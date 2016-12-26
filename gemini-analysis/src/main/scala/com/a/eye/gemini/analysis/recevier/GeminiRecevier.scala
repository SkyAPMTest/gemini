package com.a.eye.gemini.analysis.recevier

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.LogManager
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext

import com.a.eye.gemini.analysis.executer.model.RecevierData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.util.JsonUtil
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.TimeSlotUtil
import com.a.eye.gemini.analysis.util.UrlUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.bson.types.ObjectId
import org.bson.Document
import org.apache.spark.SparkContext
import com.a.eye.gemini.analysis.util.SparkContextSingleton
import com.a.eye.gemini.analysis.config.SparkConfig
import com.a.eye.gemini.analysis.config.SparkConfig
import com.mongodb.casbah.commons.MongoDBObject
import com.a.eye.gemini.analysis.util.GeminiMongoClient

class GeminiRecevier extends GeminiAbstractRecevier("gemini-sniffer-app", "gemini-sniffer-topic", 0, "gemini-sniffer-group", "sniffer_idx", "sniffer") {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def buildData(streamingContext: StreamingContext, rdd: RDD[ConsumerRecord[Long, String]], partition: Int, periodTime: String): Array[(RecevierPairsData)] = {
    val reqData = rdd.filter(record => !isResData(record)).map(record => { buildReqData(record, partition) }).collect()
    val resData = rdd.filter(record => isResData(record)).map(record => { buildResData(record, partition) })
    logger.info("请求数据条数：%d", reqData.length)
    logger.info("响应数据条数：%d", resData.count())

    val req_res_pairs = resData.map(resRow => {
      val pairsData = new RecevierPairsData()
      val resSeq = changeSeqHead(resRow.tcpSeq)
      val jedis = RedisClient.pool.getResource

      pairsData.messageId = resRow.messageId
      pairsData.tcpTime = resRow.tcpTime
      pairsData.tcpSeq = resSeq
      pairsData.isPair = false

      if (jedis.exists(resSeq) && !jedis.exists(createPairKey(resSeq))) {
        val gson = new Gson()
        val request = jedis.get(String.valueOf(resSeq));
        val reqJson = gson.fromJson(request, classOf[JsonObject])
        jedis.setex(resRow.tcpSeq, TimeSlotUtil.getRedisExSecond(TimeSlotUtil.Atom), "yes")
        jedis.setex(createPairKey(resSeq), TimeSlotUtil.getRedisExSecond(TimeSlotUtil.Atom), "yes")

        logger.info("requestJson: %s ", reqJson.toString())
        if (reqJson.has("req_RequestUrl")) {
          val url = UrlUtil.removeParameters(reqJson.get("req_RequestUrl").getAsString)
          reqJson.remove("req_RequestUrl")
          reqJson.addProperty("req_RequestUrl", url)
        } else {
          reqJson.addProperty("req_RequestUrl", "/")
        }

        pairsData.reqData = JsonUtil.jsonObject2Map(reqJson)
        pairsData.resData = resRow.data

        val startTime = pairsData.reqData.get("tcp_time").get.toLong
        val endTime = pairsData.resData.get("tcp_time").get.toLong
        val cost = endTime - startTime
        pairsData.reqData = pairsData.reqData ++ Map("cost" -> cost.toString())
        logger.debug("地址：%s%s, 响应时间：%d", reqJson.get("req_Host").getAsString, reqJson.get("req_RequestUrl").getAsString, cost)

        pairsData.isPair = true
      }
      RedisClient.pool.returnResource(jedis)
      pairsData
    }).filter(_.isPair).collect()

    val length = req_res_pairs.length
    logger.info("成功配对的数据条数：%d", length)

    req_res_pairs.foreach(pairsData => {
      val mongoData = MongoDBObject(
        "_id" -> pairsData.messageId,
        "req_data" -> pairsData.reqData.mkString(","),
        "res_data" -> pairsData.resData.mkString(","),
        "tcp_time" -> pairsData.tcpTime,
        "tcp_seq" -> pairsData.tcpSeq,
        "tcp_ack" -> pairsData.tcpAck)

      GeminiMongoClient.db("pairs").insert(mongoData)
    })

    req_res_pairs
  }

  private def isResData(record: ConsumerRecord[Long, String]): Boolean = {
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    messageJson.get("is_res").getAsBoolean
  }

  private def buildReqData(record: ConsumerRecord[Long, String], partition: Int): (RecevierData) = {
    val reqData = new RecevierData()
    val gson = new Gson()
    val jsonData = gson.fromJson(record.value(), classOf[JsonObject])
    val mapData = JsonUtil.jsonObject2Map(jsonData)

    reqData.messageId = record.key()
    reqData.data = mapData
    reqData.tcpSeq = "REQ-" + reqData.data.get("tcp_seq")
    reqData.tcpAck = "REQ-" + reqData.data.get("tcp_ack")
    reqData.tcpTime = reqData.data.getOrElse("tcp_time", "0").toLong

    val jedis = RedisClient.pool.getResource
    jedis.setex(reqData.tcpAck, TimeSlotUtil.getRedisExSecond(TimeSlotUtil.Atom), record.value())
    RedisClient.pool.returnResource(jedis)

    reqData
  }

  private def buildResData(record: ConsumerRecord[Long, String], partition: Int): (RecevierData) = {
    val resData = new RecevierData()
    val gson = new Gson()
    val jsonData = gson.fromJson(record.value(), classOf[JsonObject])
    val mapData = JsonUtil.jsonObject2Map(jsonData)

    resData.messageId = record.key()
    resData.data = mapData
    resData.tcpSeq = "RES-" + resData.data.get("tcp_seq")
    resData.tcpAck = "RES-" + resData.data.get("tcp_ack")
    resData.tcpTime = resData.data.getOrElse("tcp_time", "0").toLong

    //    val jedis = RedisClient.pool.getResource
    //    jedis.setex(resData.tcpSeq, TimeSlotUtil.getRedisExSecond(TimeSlotUtil.Atom), "no")
    //    RedisClient.pool.returnResource(jedis)

    resData
  }

  private def changeSeqHead(resSeq: String): String = {
    "REQ-" + resSeq.split("-").apply(1)
  }

  private def createPairKey(resSeq: String): String = {
    "PAIR-" + resSeq
  }
}