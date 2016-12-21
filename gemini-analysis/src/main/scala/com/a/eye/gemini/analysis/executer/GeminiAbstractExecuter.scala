package com.a.eye.gemini.analysis.executer

import org.apache.spark.rdd.RDD
import com.google.gson.JsonObject
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.util.TimeSlotUtil

abstract class GeminiAbstractExecuter extends Serializable {

  def buildIndicatorData(data: RDD[(RecevierPairsData)], partition: Int): RDD[(IndicatorData)]

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int)

  def buildAnalysisSlotData(data: RDD[(IndicatorData)], partition: Int, timeSlotUtil: TimeSlotUtil): RDD[(String, Int)]

  def buildAnalysisAtomData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)]

  def buildAnalysisHourData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)]

  def buildAnalysisDayData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)]

  def buildAnalysisWeekData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)]

  def buildAnalysisMonthData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)]

  def saveAnalysisSlotData(data: RDD[(String, Int)], partition: Int, slotType: String)

  def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int)

  def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int)
}