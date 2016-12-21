package com.a.eye.gemini.analysis.executer

import org.apache.spark.rdd.RDD
import com.google.gson.JsonObject
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.util.TimeSlotUtil

abstract class GeminiAbstractExecuter extends Serializable {

  def buildIndicatorData(data: RDD[(RecevierPairsData)], partition: Int): RDD[(IndicatorData)]

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int)

  def buildAnalysisHostSlotData(data: RDD[(String, Int)], indType: String): RDD[(String, Int)]

  def buildAnalysisIndiSlotData(data: RDD[(IndicatorData)], partition: Int, timeSlotUtil: TimeSlotUtil): RDD[(String, Int)]

  def saveAnalysisIndiData(data: RDD[(String, Int)], partition: Int, slotType: String)

  def saveAnalysisHostData(data: RDD[(String, Int)], partition: Int, slotType: String)

  def analysisAtomData(data: RDD[(IndicatorData)], partition: Int)

  def analysisHourData(data: RDD[(IndicatorData)], partition: Int)

  def analysisDayData(data: RDD[(IndicatorData)], partition: Int)

  def analysisWeekData(data: RDD[(IndicatorData)], partition: Int)

  def analysisMonthData(data: RDD[(IndicatorData)], partition: Int)
}