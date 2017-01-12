package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.TimeSlotUtil
import com.a.eye.gemini.analysis.util.AtomTimeSlotUtil
import com.a.eye.gemini.analysis.config.GeminiConfig
import com.a.eye.gemini.analysis.util.DayTimeSlotUtil
import com.a.eye.gemini.analysis.util.HourTimeSlotUtil
import com.a.eye.gemini.analysis.util.MonthTimeSlotUtil
import com.a.eye.gemini.analysis.util.WeekTimeSlotUtil
import com.a.eye.gemini.analysis.util.ReduceKeyUtil

abstract class UniqueIndicatorExecuter(indKey: String, isUseIndValue: Boolean, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, isUseIndValue: Boolean, indKeyName: String) {

  override def buildAnalysisIndiSlotData(data: RDD[(IndicatorData)], partition: Int, timeSlotUtil: TimeSlotUtil, keyInDbName: String, slotType: String): RDD[(String, Long)] = {
    data.map(indicatorData => {
      val timeSlot = timeSlotUtil.compareSlotTime(indicatorData.tcpTime)
      if (isUseIndValue) {
        val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData.host, keyInDbName)
        (indKey, indicatorData.indKey.toLong)
      } else {
        val jedis = RedisClient.pool.getResource
        val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData.host, indicatorData.indKey)

        var reduceValue = 1l
        if (jedis.exists(indKey)) {
          reduceValue = 0l
        } else {
          jedis.setex(indKey, TimeSlotUtil.getRedisExSecond(slotType), String.valueOf(reduceValue))
        }
        RedisClient.pool.returnResource(jedis)

        (indKey, reduceValue)
      }
    }).reduceByKey(_ + _)
  }

  override def buildAnalysisHostSlotData(data: RDD[(String, Long)], slotType: String): RDD[(String, Long)] = {
    data.map(analysisIndiSlotData => {
      val jedis = RedisClient.pool.getResource
      val analysisKey = analysisIndiSlotData._1

      var hostKey = analysisKey
      if (!isUseIndValue) {
        hostKey = ReduceKeyUtil.parseIndiKeyToHostKey(analysisKey)
      }

      var analysisVal = 1l
      if (jedis.exists(hostKey)) {
        analysisVal = 0l
      } else {
        jedis.setex(analysisKey, TimeSlotUtil.getRedisExSecond(slotType), String.valueOf(analysisVal))
      }
      RedisClient.pool.returnResource(jedis)
      (hostKey, analysisVal)
    }).reduceByKey(_ + _)
  }
}