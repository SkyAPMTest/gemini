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

abstract class UniqueIndicatorExecuter(indKey: String, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, indKeyName: String) {

  override def buildAnalysisHostSlotData(data: RDD[(String, Int)], slotType: String): RDD[(String, Int)] = {
    data.map(analysisIndiSlotData => {
      val jedis = RedisClient.pool.getResource
      val analysisKey = analysisIndiSlotData._1

      val hostKey = ReduceKeyUtil.parseIndiKeyToHostKey(analysisKey)

      var analysisVal = 1
      if (jedis.exists(hostKey)) {
        analysisVal = 0
      } else {
        jedis.setex(analysisKey, TimeSlotUtil.getRedisExSecond(slotType), String.valueOf(analysisVal))
      }
      RedisClient.pool.returnResource(jedis)
      (hostKey, analysisVal)
    }).reduceByKey(_ + _)
  }
}