package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

import com.a.eye.gemini.analysis.util.RedisClient
import com.google.gson.JsonObject
import org.apache.kafka.clients.consumer.ConsumerRecord
import com.a.eye.gemini.analysis.executer.model.RecevierData

class UvIndicatorExecuter extends UniqueIndicatorExecuter("eth_source", false, "uv") {
  def validateReq(reqJson: JsonObject): Boolean = {
    reqJson.has("eth_source")
  }

  def validateRes(resJson: JsonObject): Boolean = {
    true
  }
}