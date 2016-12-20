package com.a.eye.gemini.analysis.executer

import org.apache.spark.rdd.RDD
import com.google.gson.JsonObject
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData

abstract class GeminiAbstractExecuter {

  def buildIndicatorData(data: RDD[(RecevierPairsData)], partition: Int): RDD[(IndicatorData)]

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int)

  def buildAnalysisAtomData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)]

  def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int)
}