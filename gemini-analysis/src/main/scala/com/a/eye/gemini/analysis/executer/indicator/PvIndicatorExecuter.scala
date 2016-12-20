package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import org.elasticsearch.spark._
import org.elasticsearch.spark.rdd.Metadata

import com.a.eye.gemini.analysis.util.RedisClient
import com.google.gson.JsonObject

class PvIndicatorExecuter extends CumulativeIndicatorExecuter("req_RequestUrl", "pv") {

  def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int) {

  }
}