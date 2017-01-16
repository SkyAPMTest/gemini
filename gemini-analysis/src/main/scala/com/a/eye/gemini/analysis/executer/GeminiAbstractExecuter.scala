package com.a.eye.gemini.analysis.executer

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.util.TimeSlotUtil
import org.apache.spark.SparkContext
import org.apache.kafka.clients.consumer.ConsumerRecord
import com.a.eye.gemini.analysis.executer.model.RecevierData
import com.google.gson.JsonObject

abstract class GeminiAbstractExecuter extends Serializable {

  def filterIndicatorData(data: RDD[Map[String, String]], partition: Int): RDD[Map[String, String]]

  def buildIndicatorData(data: RDD[Map[String, String]], partition: Int): RDD[(IndicatorData)]

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int, periodTime: String)

  def buildAnalysisHostSlotData(data: RDD[(Long, Map[String, String])], timeSlotUtil: TimeSlotUtil, slotType: String): RDD[(String, Long)]

  def buildAnalysisIndiSlotData(data: RDD[(Long, Map[String, String])], timeSlotUtil: TimeSlotUtil, keyInDbName: String, slotType: String): RDD[(String, Long)]

  def saveAnalysisIndiData(data: RDD[(String, Long)], partition: Int, slotType: String, periodTime: String)

  def saveAnalysisHostData(data: RDD[(String, Long)], slotType: String, periodTime: String)

  //  def analysisAtomData(data: RDD[(IndicatorData)], partition: Int, periodTime: String)

  //  def analysisHourData(data: RDD[(IndicatorData)], partition: Int, periodTime: String)

  def analysisDayData(data: RDD[(Long, Map[String, String])], periodTime: String)

  //  def analysisWeekData(data: RDD[(IndicatorData)], partition: Int, periodTime: String)

  //  def analysisMonthData(data: RDD[(IndicatorData)], partition: Int, periodTime: String)
}