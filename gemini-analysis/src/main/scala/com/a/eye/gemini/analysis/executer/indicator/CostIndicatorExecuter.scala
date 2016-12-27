package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import org.apache.kafka.clients.consumer.ConsumerRecord
import com.a.eye.gemini.analysis.executer.model.RecevierData
import com.google.gson.JsonObject

class CostIndicatorExecuter extends UniqueIndicatorExecuter("cost", true, "cost") {

  def validateReq(reqJson: JsonObject): Boolean = {
    reqJson.has("tcp_time")
  }

  def validateRes(resJson: JsonObject): Boolean = {
    resJson.has("tcp_time")
  }
}