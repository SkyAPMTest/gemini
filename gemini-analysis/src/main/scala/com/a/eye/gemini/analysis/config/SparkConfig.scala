package com.a.eye.gemini.analysis.config

import org.apache.spark.SparkConf
import com.typesafe.config.Config

class SparkConfig(conf: Config) {
  val sparkConfig = new SparkConf();
  sparkConfig.set("es.index.auto.create", conf.getString("es.index.auto.create"))
  //  sparkConfig.set("es.nodes", "10.1.241.18:9200,10.1.241.19:9200,10.1.241.20:9200")
  sparkConfig.set("es.nodes", conf.getString("es.nodes"))
  sparkConfig.set("spark.streaming.kafka.consumer.cache.initialCapacity", conf.getString("spark.streaming.kafka.consumer.cache.initialCapacity"))
  sparkConfig.set("spark.streaming.kafka.consumer.cache.maxCapacity", conf.getString("spark.streaming.kafka.consumer.cache.maxCapacity"))
  sparkConfig.set("spark.streaming.backpressure.enabled", conf.getString("spark.streaming.backpressure.enabled"))
  sparkConfig.set("spark.streaming.kafka.maxRatePerPartition", conf.getString("spark.streaming.kafka.maxRatePerPartition"))
}