package com.a.eye.gemini.analysis.config

import org.apache.spark.SparkConf
import com.typesafe.config.Config

class SparkConfig(conf: Config) {
  val sparkConf = new SparkConf();
  sparkConf.set("es.index.auto.create", conf.getString("es.index.auto.create"))
  //  sparkConfig.set("es.nodes", "10.1.241.18:9200,10.1.241.19:9200,10.1.241.20:9200")
  sparkConf.set("es.nodes", conf.getString("es.nodes"))
  sparkConf.set("spark.logConf", conf.getString("spark.logConf"))
  sparkConf.set("spark.streaming.receiver.writeAheadLog.enable", conf.getString("spark.streaming.receiver.writeAheadLog.enable"))
  sparkConf.set("spark.streaming.kafka.consumer.cache.initialCapacity", conf.getString("spark.streaming.kafka.consumer.cache.initialCapacity"))
  sparkConf.set("spark.streaming.kafka.consumer.cache.maxCapacity", conf.getString("spark.streaming.kafka.consumer.cache.maxCapacity"))
  sparkConf.set("spark.streaming.backpressure.enabled", conf.getString("spark.streaming.backpressure.enabled"))
  sparkConf.set("spark.streaming.receiver.maxRate", conf.getString("spark.streaming.receiver.maxRate"))
  sparkConf.set("spark.streaming.kafka.maxRatePerPartition", conf.getString("spark.streaming.kafka.maxRatePerPartition"))
}