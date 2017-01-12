package com.a.eye.gemini.analysis.config

import org.apache.spark.SparkConf
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object SparkConfig {

  private val conf = ConfigFactory.load()

  val sparkConf = new SparkConf();
  sparkConf.set("spark.mongodb.output.uri", "mongodb://master:27017/")
  sparkConf.set("spark.mongodb.output.database", "gemini")
  sparkConf.set("spark.mongodb.output.collection", "gemini")

  sparkConf.set("spark.streaming.receiver.writeAheadLog.enable", conf.getString("spark.streaming.receiver.writeAheadLog.enable"))
  sparkConf.set("spark.streaming.kafka.consumer.cache.initialCapacity", conf.getString("spark.streaming.kafka.consumer.cache.initialCapacity"))
  sparkConf.set("spark.streaming.kafka.consumer.cache.maxCapacity", conf.getString("spark.streaming.kafka.consumer.cache.maxCapacity"))
  sparkConf.set("spark.streaming.backpressure.enabled", conf.getString("spark.streaming.backpressure.enabled"))
  sparkConf.set("spark.streaming.receiver.maxRate", conf.getString("spark.streaming.receiver.maxRate"))
  sparkConf.set("spark.streaming.kafka.maxRatePerPartition", conf.getString("spark.streaming.kafka.maxRatePerPartition"))
  sparkConf.set("spark.streaming.kafka.consumer.poll.ms", conf.getString("spark.streaming.kafka.consumer.poll.ms"))
}