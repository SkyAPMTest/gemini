package com.a.eye.gemini.analysis.recevier

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.LogManager

import com.a.eye.gemini.analysis.util.RedisClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.spark.rdd.RDD
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.Metadata
import breeze.util.partition
import com.a.eye.gemini.analysis.util.DateUtil
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.util.UrlUtil

class GeminiRecevier extends GeminiAbstractRecevier("sniffer-recevier-app", "sniffer-recevier-topic", 0, "sniffer-recevier-group", "sniffer_idx", "sniffer") {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  private val Pairs_Index_Name = "gemini_pairs_idx"

  private val Pairs_Type_Name = "record"

  private val Pairs_Es = Pairs_Index_Name + "/" + Pairs_Type_Name

  override def buildData(rdd: RDD[ConsumerRecord[Long, String]], partition: Int): RDD[(RecevierPairsData)] = {
    val reqData = rdd.filter(record => !isResData(record)).map(record => { buildReqData(record, partition) })
    val resData = rdd.filter(record => isResData(record)).map(record => { buildResData(record, partition) })

    reqData.foreach(f => (println("请求" + f.messageId)))

    val req_res_pairs = resData.map(resRow => {
      val pairsData = new RecevierPairsData()
      val resSeq = resRow.seq
      val jedis = RedisClient.pool.getResource
      val request = jedis.get(String.valueOf(resSeq));
      RedisClient.pool.returnResource(jedis)

      pairsData.messageId = resRow.messageId
      pairsData.tcpTime = resRow.tcpTime
      pairsData.seq = resSeq

      if (request != null) {
        val gson = new Gson()
        val reqJson = gson.fromJson(request, classOf[JsonObject])

        val pairs = new JsonObject();
        pairs.add("request", reqJson)
        pairs.add("response", resRow.pairs)
        pairsData.pairs = pairs

        pairsData
      } else {
        pairsData.pairs = null
        pairsData
      }
    }).filter(pairsData => pairsData.pairs != null)

    val count = req_res_pairs.count()
    logger.info("成功配对的数据条数：%d", count)

    req_res_pairs.map(pairsData => {
      (Map(Metadata.ID -> pairsData.messageId), Map(
        "partition" -> partition,
        "seq" -> pairsData.seq,
        "pairs" -> pairsData.pairs.toString(),
        "create_date" -> pairsData.tcpTime))
    }).saveToEsWithMeta(Pairs_Es)

    req_res_pairs
  }

  private def isResData(record: ConsumerRecord[Long, String]): Boolean = {
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    messageJson.get("is_res").getAsBoolean
  }

  private def buildReqData(record: ConsumerRecord[Long, String], partition: Int): (RecevierPairsData) = {
    val reqData = new RecevierPairsData()
    val gson = new Gson()
    val reqJson = gson.fromJson(record.value(), classOf[JsonObject])
    val url = UrlUtil.removeParameters(reqJson.get("req_RequestUrl").getAsString)
    reqJson.remove("req_RequestUrl")
    reqJson.addProperty("req_RequestUrl", url)

    reqData.messageId = record.key()
    reqData.pairs = reqJson
    reqData.seq = reqData.pairs.get("tcp_seq").getAsString
    reqData.tcpTime = reqData.pairs.get("tcp_time").getAsLong
    logger.info("seq=%s", reqData.seq)

    val jedis = RedisClient.pool.getResource
    jedis.setex(reqData.seq, 120, record.value())
    RedisClient.pool.returnResource(jedis)

    reqData
  }

  private def buildResData(record: ConsumerRecord[Long, String], partition: Int): (RecevierPairsData) = {
    val resData = new RecevierPairsData()
    val gson = new Gson()
    resData.messageId = record.key()
    resData.pairs = gson.fromJson(record.value(), classOf[JsonObject])
    resData.seq = resData.pairs.get("tcp_ack").getAsString
    resData.tcpTime = resData.pairs.get("tcp_time").getAsLong
    resData
  }
}