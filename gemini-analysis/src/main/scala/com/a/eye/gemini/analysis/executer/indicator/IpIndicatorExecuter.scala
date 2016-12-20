package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

class IpIndicatorExecuter extends UniqueIndicatorExecuter("ip_source", "ip") {

  def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int) {

  }
}