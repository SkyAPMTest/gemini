package com.a.eye.gemini.analysis.config

import org.apache.spark.SparkConf
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object SparkConfig {

  private val conf = ConfigFactory.load()

  val sparkConf = new SparkConf();
  sparkConf.set("es.index.auto.create", conf.getString("es.index.auto.create"))
  sparkConf.set("es.index.read.missing.as.empty", conf.getString("es.index.read.missing.as.empty"))

  sparkConf.set("spark.mongodb.output.uri", "mongodb://localhost:27017/")
  sparkConf.set("spark.mongodb.output.database", "gemini")
  sparkConf.set("spark.mongodb.output.collection", "test")

  sparkConf.set("es.nodes", conf.getString("es.nodes"))
  sparkConf.set("es.port", conf.getString("es.port"))
  sparkConf.set("spark.streaming.receiver.writeAheadLog.enable", conf.getString("spark.streaming.receiver.writeAheadLog.enable"))
  sparkConf.set("spark.streaming.kafka.consumer.cache.initialCapacity", conf.getString("spark.streaming.kafka.consumer.cache.initialCapacity"))
  sparkConf.set("spark.streaming.kafka.consumer.cache.maxCapacity", conf.getString("spark.streaming.kafka.consumer.cache.maxCapacity"))
  sparkConf.set("spark.streaming.backpressure.enabled", conf.getString("spark.streaming.backpressure.enabled"))
  sparkConf.set("spark.streaming.receiver.maxRate", conf.getString("spark.streaming.receiver.maxRate"))
  sparkConf.set("spark.streaming.kafka.maxRatePerPartition", conf.getString("spark.streaming.kafka.maxRatePerPartition"))
}