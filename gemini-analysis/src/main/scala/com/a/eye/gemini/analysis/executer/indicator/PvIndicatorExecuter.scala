package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.util.RedisClient
import com.google.gson.JsonObject
import org.apache.kafka.clients.consumer.ConsumerRecord
import com.a.eye.gemini.analysis.executer.model.RecevierData

class PvIndicatorExecuter extends CumulativeIndicatorExecuter("req_RequestUrl", false, "pv") {

  def validateReq(reqJson: JsonObject): Boolean = {
//    reqJson.has("req_RequestUrl")
    true
  }

  def validateRes(resJson: JsonObject): Boolean = {
    true
  }
}