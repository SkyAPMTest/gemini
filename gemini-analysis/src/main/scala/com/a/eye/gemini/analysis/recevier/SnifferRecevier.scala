package com.a.eye.gemini.analysis.recevier

import org.apache.logging.log4j.LogManager;
import org.apache.kafka.clients.consumer.ConsumerRecord
import com.google.gson.Gson
import scala.io.Source
import org.elasticsearch.spark.rdd.Metadata
import com.google.gson.JsonObject
import com.a.eye.gemini.analysis.base.RecevierBase
import java.util.HashMap
import redis.clients.jedis.Jedis
import com.a.eye.gemini.analysis.util.RedisClient

class SnifferRecevier extends RecevierBase("sniffer-recevier-app", "sniffer-recevier-topic", "sniffer-recevier-group", "sniffer_idx", "sniffer") {

  override def isResData(record: ConsumerRecord[String, String]): Boolean = {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    messageJson.get("is_res").getAsBoolean
  }

  override def buildReqData(record: ConsumerRecord[String, String]): (String, JsonObject) = {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])
    val seq = messageJson.get("tcp_seq").getAsString
    logger.info("seq=%s", seq)

    val jedis = RedisClient.pool.getResource
    jedis.setex(seq, 120, record.value())
    RedisClient.pool.returnResource(jedis)

    (seq, messageJson)
  }

  override def buildResData(record: ConsumerRecord[String, String]): (String, JsonObject) = {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])

    val seq = messageJson.get("tcp_ack").getAsString

    (seq, messageJson)
  }
}