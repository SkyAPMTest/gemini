package com.a.eye.gemini.analysis.recevier

import org.apache.logging.log4j.LogManager;
import org.apache.kafka.clients.consumer.ConsumerRecord
import com.google.gson.Gson
import scala.io.Source
import org.elasticsearch.spark.rdd.Metadata
import com.google.gson.JsonObject
import com.a.eye.gemini.analysis.base.RecevierBase
import java.util.HashMap

class SnifferRecevier extends RecevierBase("sniffer-recevier-app", "sniffer-recevier-topic", "sniffer-recevier-group", "sniffer_idx", "sniffer") {

  override def validateData(record: ConsumerRecord[String, String]): Boolean = {
    true
  }

  override def buildEsData(record: ConsumerRecord[String, String]): (Map[Metadata, String], Map[String, String]) = {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val gson = new Gson()
    val messageJson = gson.fromJson(record.value(), classOf[JsonObject])

    var dataMap: Map[String, String] = Map()

    val iter = messageJson.entrySet().iterator()
    while (iter.hasNext()) {
      val element = iter.next()
      val key = element.getKey
      val value = element.getValue

      println("key: " + key)
      println("value: " + value)

      dataMap += (key -> value.getAsString)
    }

    dataMap.foreach(f => (println("key=" + f._1 + ",value=" + f._2)))

    (Map(Metadata.ID -> record.key()), dataMap)
  }
}