package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.util.RedisClient
import com.google.gson.JsonObject

class PvIndicatorExecuter extends CumulativeIndicatorExecuter("req_RequestUrl", "pv") {
}