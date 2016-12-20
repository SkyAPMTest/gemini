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

class GeminiRecevier extends GeminiAbstractRecevier("sniffer-recevier-app", "sniffer-recevier-topic", 0, "sniffer-recevier-group", "sniffer_idx", "sniffer") {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  private val Pairs_Index_Name = "gemini_pairs_idx"

  private val Pairs_Type_Name = "record"

  private val Pairs_Es = Pairs_Index_Name + "/" + Pairs_Type_Name

  override def buildData(rdd: RDD[ConsumerRecord[Long, String]], partition: Int): RDD[(Long, String, Long, JsonObject, String)] = {
    val reqData = rdd.filter(record => !isResData(record)).map(record => { buildReqData(record, partition) })
    val resData = rdd.filter(record => isResData(record)).map(record => { buildResData(record, partition) })

    reqData.foreach(f => (println("请求" + f._1 + "------" + f._2)))

    val req_res_pairs = resData.map(response => {
      val resSeq = response._2
      val jedis = RedisClient.pool.getResource
      val request = jedis.get(String.valueOf(resSeq));
      RedisClient.pool.returnResource(jedis)

      val messageId = response._1
      val tcpTime = response._3
      val slotTime = response._5

      if (request != null) {
        val gson = new Gson()
        val reqJson = gson.fromJson(request, classOf[JsonObject])

        val pairs = new JsonObject();
        pairs.add("request", reqJson)
        pairs.add("response", response._4)

        (messageId, resSeq, tcpTime, pairs, slotTime)
      } else {
        (messageId, resSeq, tcpTime, null, slotTime)
      }
    }).filter(f => f._4 != null)

    val count = req_res_pairs.count()
    logger.info("成功配对的数据条数：%d", count)

    req_res_pairs.map(pairs => {
      (Map(Metadata.ID -> pairs._1), Map(
        "partition" -> partition,
        "seq" -> pairs._2,
        "pairs" -> pairs._4.toString(),
        "slot_time" -> pairs._5,
        "create_date" -> pairs._3))
    }).saveToEsWithMeta(Pairs_Es)

    req_res_pairs
  }

  private def isResData(record: ConsumerRecord[Long, String]): Boolean = {
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    messageJson.get("is_res").getAsBoolean
  }

  private def buildReqData(record: ConsumerRecord[Long, String], partition: Int): (Long, String, Long, JsonObject, String) = {
    val gson = new Gson()
    val messageId = record.key()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    val seq = messageJson.get("tcp_seq").getAsString
    val tcpTime = messageJson.get("tcp_time").getAsLong
    logger.info("seq=%s", seq)

    val jedis = RedisClient.pool.getResource
    jedis.setex(seq, 120, record.value())
    RedisClient.pool.returnResource(jedis)

    (messageId, seq, tcpTime, messageJson, null)
  }

  private def buildResData(record: ConsumerRecord[Long, String], partition: Int): (Long, String, Long, JsonObject, String) = {
    val gson = new Gson()
    val messageId = record.key()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    val seq = messageJson.get("tcp_ack").getAsString
    val tcpTime = messageJson.get("tcp_time").getAsLong
    val slotTime = DateUtil.compareSlotTime(partition, tcpTime)
    (messageId, seq, tcpTime, messageJson, slotTime)
  }
}