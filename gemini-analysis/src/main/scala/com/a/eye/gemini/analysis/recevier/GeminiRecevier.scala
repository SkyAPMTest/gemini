package com.a.eye.gemini.analysis.recevier

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.logging.log4j.LogManager
import org.apache.spark.HashPartitioner
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.streaming.StreamingContext

import com.a.eye.gemini.analysis.util.JsonUtil
import com.google.gson.Gson
import com.google.gson.JsonObject
import Array._

class GeminiRecevier extends GeminiAbstractRecevier("gemini-sniffer-app", "gemini-sniffer-topic", 14, "gemini-sniffer-group") {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def buildData(streamingContext: StreamingContext, rdd: RDD[ConsumerRecord[Long, Array[Byte]]], periodTime: String): RDD[(Long, Map[String, String])] = {
    val recordMapData = rdd.map(record => {
      var data: Map[String, String] = Map();

      val snifferData = record.value()
      var offset = 0

      val req_host_length = snifferData.apply(offset)
      offset += 1;
      val req_host_byte = new Array[Byte](req_host_length)
      System.arraycopy(snifferData, offset, req_host_byte, 0, req_host_length)
      offset += req_host_length;

      val req_requesturl_length = snifferData.apply(offset)
      offset += 1;
      val req_requesturl_byte = new Array[Byte](req_requesturl_length)
      System.arraycopy(snifferData, offset, req_requesturl_byte, 0, req_requesturl_length)
      offset += req_requesturl_length;

      val req_tcp_seq_length = snifferData.apply(offset)
      offset += 1;
      val req_tcp_seq_byte = new Array[Byte](req_tcp_seq_length)
      System.arraycopy(snifferData, offset, req_tcp_seq_byte, 0, req_tcp_seq_length)
      offset += req_tcp_seq_length;

      val req_tcp_time_length = snifferData.apply(offset)
      offset += 1;
      val req_tcp_time_byte = new Array[Byte](req_tcp_time_length)
      System.arraycopy(snifferData, offset, req_tcp_time_byte, 0, req_tcp_time_length)
      offset += req_tcp_time_length;

      data += ("message_id" -> record.key().toString())
      data += ("req_host" -> new String(req_host_byte))
      data += ("req_requesturl" -> new String(req_requesturl_byte))
      data += ("req_tcp_seq" -> new String(req_tcp_seq_byte))
      data += ("req_tcp_time" -> new String(req_tcp_time_byte))
      (record.key(), data)
    }).cache()
    //    .filter(f => {
    //      f.contains("req_host") && f.contains("req_tcp_seq") && f.contains("req_tcp_time") && f.contains("req_requesturl")
    //    })

    //    val count = recordMapData.count()
    //    logger.info("数据条数：%d", count)
    recordMapData
  }
}