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
import org.apache.logging.log4j.LogManager
import org.apache.spark.HashPartitioner

abstract class CumulativeIndicatorExecuter(indKey: String, isUseIndValue: Boolean, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, isUseIndValue: Boolean, indKeyName: String) {

  override def buildAnalysisIndiSlotData(data: RDD[(Long, Map[String, String])], timeSlotUtil: TimeSlotUtil, keyInDbName: String, slotType: String): RDD[(String, Long)] = {
    data.mapPartitions(partition => {
      partition.map(indicatorData => {
        val timeSlot = timeSlotUtil.compareSlotTime(indicatorData._2.get("req_tcp_time").get.toLong)
        if (isUseIndValue) {
          val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData._2.get("req_host").get, keyInDbName)
          (indKey, 1l)
          //        (indKey, indicatorData.indKey.toLong)
        } else {
          val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData._2.get("req_host").get, keyInDbName)
          (indKey, 1l)
        }
      })
    }).reduceByKey(_ + _)
  }

  override def buildAnalysisHostSlotData(data: RDD[(Long, Map[String, String])], timeSlotUtil: TimeSlotUtil, slotType: String): RDD[(String, Long)] = {
    data.mapPartitions(partition => {
      partition.map(analysisIndiSlotData => {
        val timeSlot = timeSlotUtil.compareSlotTime(analysisIndiSlotData._2.get("req_tcp_time").get.toLong)
        val analysisKey = timeSlot + ReduceKeyUtil.Split_Str + analysisIndiSlotData._2.get("req_host").get
        val analysisVal = 1l

        var hostKey = analysisKey
        if (!isUseIndValue) {
          //        hostKey = ReduceKeyUtil.parseIndiKeyToHostKey(analysisKey)
        }

        (hostKey, analysisVal)
      })
    }).reduceByKey(_ + _)
  }
}