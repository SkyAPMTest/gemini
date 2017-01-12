package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.util.AtomTimeSlotUtil
import com.a.eye.gemini.analysis.util.DayTimeSlotUtil
import com.a.eye.gemini.analysis.util.HourTimeSlotUtil
import com.a.eye.gemini.analysis.util.MonthTimeSlotUtil
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.ReduceKeyUtil
import com.a.eye.gemini.analysis.util.WeekTimeSlotUtil
import com.a.eye.gemini.analysis.util.TimeSlotUtil

abstract class CumulativeIndicatorExecuter(indKey: String, isUseIndValue: Boolean, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, isUseIndValue: Boolean, indKeyName: String) {

  override def buildAnalysisIndiSlotData(data: RDD[(IndicatorData)], partition: Int, timeSlotUtil: TimeSlotUtil, keyInDbName: String, slotType: String): RDD[(String, Long)] = {
    data.map(indicatorData => {
      val timeSlot = timeSlotUtil.compareSlotTime(indicatorData.tcpTime)
      if (isUseIndValue) {
        val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData.host, keyInDbName)
        (indKey, indicatorData.indKey.toLong)
      } else {
        val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData.host, indicatorData.indKey)
        (indKey, 1l)
      }
    }).reduceByKey(_ + _)
  }

  override def buildAnalysisHostSlotData(data: RDD[(String, Long)], slotType: String): RDD[(String, Long)] = {
    data.map(analysisIndiSlotData => {
      val analysisKey = analysisIndiSlotData._1
      val analysisVal = analysisIndiSlotData._2

      var hostKey = analysisKey
      if (!isUseIndValue) {
        hostKey = ReduceKeyUtil.parseIndiKeyToHostKey(analysisKey)
      }

      (hostKey, analysisVal)
    }).reduceByKey(_ + _)
  }
}