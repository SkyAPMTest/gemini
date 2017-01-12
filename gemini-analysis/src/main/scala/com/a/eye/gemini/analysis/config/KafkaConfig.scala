package com.a.eye.gemini.analysis.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object KafkaConfig {

  private val conf = ConfigFactory.load()

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> conf.getString("bootstrap.servers"),
    "key.deserializer" -> conf.getString("key.deserializer"),
    "value.deserializer" -> conf.getString("value.deserializer"),
//    "auto.offset.reset" -> conf.getString("auto.offset.reset"),
    "max.poll.records" -> conf.getString("max.poll.records"),
    "max.partition.fetch.bytes" -> conf.getString("max.partition.fetch.bytes"),
    "fetch.max.bytes" -> conf.getString("fetch.max.bytes") 
    //    "enable.auto.commit" -> conf.getString("enable.auto.commit")
    )
}