package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import org.elasticsearch.spark.rdd.Metadata

import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.TimeSlotUtil
import com.a.eye.gemini.analysis.util.AtomTimeSlotUtil
import com.a.eye.gemini.analysis.config.GeminiConfig
import com.a.eye.gemini.analysis.util.DayTimeSlotUtil
import com.a.eye.gemini.analysis.util.HourTimeSlotUtil
import com.a.eye.gemini.analysis.util.MonthTimeSlotUtil
import com.a.eye.gemini.analysis.util.WeekTimeSlotUtil

abstract class UniqueIndicatorExecuter(indKey: String, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, indKeyName: String) {
  
  override def buildAnalysisAtomData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    this.buildAnalysisSlotData(data, partition, new AtomTimeSlotUtil())
  }

  override def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int) {
    this.saveAnalysisSlotData(data, partition, TimeSlotUtil.Atom)
  }

  override def buildAnalysisHourData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    this.buildAnalysisSlotData(data, partition, new HourTimeSlotUtil())
  }

  override def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int) {
    this.saveAnalysisSlotData(data, partition, TimeSlotUtil.Hour)
  }

  override def buildAnalysisDayData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    this.buildAnalysisSlotData(data, partition, new DayTimeSlotUtil())
  }

  override def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int) {
    this.saveAnalysisSlotData(data, partition, TimeSlotUtil.Day)
  }

  override def buildAnalysisWeekData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    this.buildAnalysisSlotData(data, partition, new WeekTimeSlotUtil())
  }

  override def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int) {
    this.saveAnalysisSlotData(data, partition, TimeSlotUtil.Week)
  }

  override def buildAnalysisMonthData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    this.buildAnalysisSlotData(data, partition, new MonthTimeSlotUtil())
  }

  override def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int) {
    this.saveAnalysisSlotData(data, partition, TimeSlotUtil.Month)
  }

  override def buildAnalysisSlotData(data: RDD[(IndicatorData)], partition: Int, timeSlotUtil: TimeSlotUtil): RDD[(String, Int)] = {
    data.map(indicatorData => {
      val timeSlot = timeSlotUtil.compareSlotTime(indicatorData.tcpTime)
      val indKey = timeSlot + "|" + indicatorData.host + "|" + indicatorData.indKey
      (indKey, 1)
    }).reduceByKey((x, y) => 1).map(row => {
      val jedis = RedisClient.pool.getResource

      val analysisKey = row._1
      val firstIdx = analysisKey.indexOf("|")
      val secondIdx = analysisKey.indexOf("|", firstIdx + 1)

      val newKey = analysisKey.substring(0, secondIdx)
      var analysisVal = 1
      if (jedis.exists(newKey)) {
        analysisVal = 0
      } else {
        if (timeSlotUtil.isInstanceOf[AtomTimeSlotUtil]) {
          val redisExSecond = GeminiConfig.intervalTime * 3
          jedis.setex(analysisKey, redisExSecond, String.valueOf(analysisVal))
        } else {
          jedis.set(analysisKey, String.valueOf(analysisVal))
        }
      }
      RedisClient.pool.returnResource(jedis)
      (newKey, analysisVal)
    }).reduceByKey(_ + _)
  }

  override def saveAnalysisSlotData(data: RDD[(String, Int)], partition: Int, slotType: String) {
    data.map(analysisRow => {
      val jedis = RedisClient.pool.getResource
      val analysisKey = analysisRow._1
      var analysisVal = analysisRow._2

      val firstIdx = analysisKey.indexOf("|")

      val timeSlot = analysisKey.substring(0, firstIdx)
      val host = analysisKey.substring(firstIdx + 1, analysisKey.length())

      if (jedis.exists(analysisKey)) {
        analysisVal = analysisVal + jedis.get(analysisKey).toInt
      } else {
        if (TimeSlotUtil.Atom.equals(slotType)) {
          val redisExSecond = GeminiConfig.intervalTime * 3
          jedis.setex(analysisKey, redisExSecond, String.valueOf(analysisVal))
        } else {
          jedis.set(analysisKey, String.valueOf(analysisVal))
        }
      }
      RedisClient.pool.returnResource(jedis)

      (Map(Metadata.ID -> analysisKey), Map(
        "partition" -> partition,
        "host" -> host,
        "timeSlot" -> timeSlot,
        indKeyName -> indKey,
        "analysisVal" -> analysisVal))
    }).saveToEsWithMeta(Indicator_Index_Name + "_" + slotType + "_idx/" + indKeyName)
  }
}