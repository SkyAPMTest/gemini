package com.a.eye.gemini.analysis.executer

import org.apache.spark.rdd.RDD
import com.google.gson.JsonObject

abstract class GeminiAbstractExecuter {

  def buildIndicatorData(data: RDD[(Long, String, Long, JsonObject, String)], partition: Int): RDD[(Long, String, String, String, Long, String)]

  def saveIndicatorData(data: RDD[(Long, String, String, String, Long, String)], partition: Int)

  def buildAnalysisAtomData(data: RDD[(Long, String, String, String, Long, String)], partition: Int): RDD[(String, Int)]

  def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int)
}