package com.a.eye.gemini.analysis.executer.indicator

import com.a.eye.gemini.analysis.executer.GeminiAbstractExecuter
import org.apache.spark.rdd.RDD
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import org.elasticsearch.spark.rdd.Metadata
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.ReduceKeyUtil
import com.a.eye.gemini.analysis.util.TimeSlotUtil
import com.a.eye.gemini.analysis.config.GeminiConfig
import com.a.eye.gemini.analysis.util.AtomTimeSlotUtil
import com.a.eye.gemini.analysis.util.MonthTimeSlotUtil
import com.a.eye.gemini.analysis.util.WeekTimeSlotUtil
import com.a.eye.gemini.analysis.util.HourTimeSlotUtil
import com.a.eye.gemini.analysis.util.DayTimeSlotUtil

abstract class CommonIndicatorExecuter(indKey: String, indKeyName: String) extends GeminiAbstractExecuter {

  val Indicator_Index_Name = "gemini_" + indKeyName + "_ind"

  override def analysisAtomData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new AtomTimeSlotUtil())
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Atom, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Atom)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Atom, periodTime)
  }

  override def analysisHourData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new HourTimeSlotUtil())
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Hour, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Hour)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Hour, periodTime)
  }

  override def analysisDayData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new DayTimeSlotUtil())
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Day, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Day)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Day, periodTime)
  }

  override def analysisWeekData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new WeekTimeSlotUtil())
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Week, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Week)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Week, periodTime)
  }

  override def analysisMonthData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new MonthTimeSlotUtil())
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Month, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Month)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Month, periodTime)
  }

  def buildIndicatorData(data: RDD[(RecevierPairsData)], partition: Int): RDD[(IndicatorData)] = {
    data.map(recevierPairsData => {
      val indicatorData = new IndicatorData()
      indicatorData.messageId = recevierPairsData.messageId
      indicatorData.resSeq = recevierPairsData.seq
      indicatorData.host = recevierPairsData.pairs.getAsJsonObject("request").get("req_Host").getAsString
      indicatorData.indKey = recevierPairsData.pairs.getAsJsonObject("request").get(indKey).getAsString
      indicatorData.indKeyName = indKeyName
      indicatorData.tcpTime = recevierPairsData.tcpTime
      (indicatorData)
    })
  }

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    data.map(indicatorData => {
      (Map(Metadata.ID -> indicatorData.messageId), Map(
        "partition" -> partition,
        "seq" -> indicatorData.resSeq,
        "host" -> indicatorData.host,
        indicatorData.indKeyName -> indicatorData.indKey,
        "create_date" -> periodTime,
        "tcp_time" -> indicatorData.tcpTime))
    }).saveToEsWithMeta(Indicator_Index_Name + "_idx/" + indKeyName)
  }

  override def buildAnalysisIndiSlotData(data: RDD[(IndicatorData)], partition: Int, timeSlotUtil: TimeSlotUtil): RDD[(String, Int)] = {
    data.map(indicatorData => {
      val timeSlot = timeSlotUtil.compareSlotTime(indicatorData.tcpTime)
      val indKey = ReduceKeyUtil.buildIndiReduceKey(timeSlot, indicatorData.host, indicatorData.indKey)
      (indKey, 1)
    }).reduceByKey(_ + _)
  }

  override def saveAnalysisIndiData(data: RDD[(String, Int)], partition: Int, slotType: String, periodTime: String) {
    data.map(analysisRow => {
      val jedis = RedisClient.pool.getResource
      val analysisKey = analysisRow._1
      var analysisVal = analysisRow._2

      val indiReduceKey = ReduceKeyUtil.parseIndiReduceKey(analysisKey)

      if (jedis.exists(analysisKey)) {
        analysisVal = analysisVal + jedis.get(analysisKey).toInt
      }
      jedis.setex(analysisKey, TimeSlotUtil.getRedisExSecond(slotType), String.valueOf(analysisVal))
      RedisClient.pool.returnResource(jedis)

      val timeSlotData = TimeSlotUtil.formatTimeSlot(indiReduceKey.timeSlot)

      (Map(Metadata.ID -> analysisKey), Map(
        "partition" -> partition,
        "host" -> indiReduceKey.host,
        "time_slot" -> indiReduceKey.timeSlot,
        "start_time" -> timeSlotData.startTime,
        "end_time" -> timeSlotData.endTime,
        indKeyName -> indiReduceKey.indKey,
        "analysis_val" -> analysisVal,
        "create_date" -> periodTime))
    }).saveToEsWithMeta(Indicator_Index_Name + "_" + slotType + "_indi_idx/" + indKeyName)
  }

  override def saveAnalysisHostData(data: RDD[(String, Int)], partition: Int, slotType: String, periodTime: String) {
    data.map(analysisRow => {
      val jedis = RedisClient.pool.getResource
      val analysisKey = analysisRow._1
      var analysisVal = analysisRow._2

      val hostReduceKey = ReduceKeyUtil.parseHostReduceKey(analysisKey)

      if (jedis.exists(analysisKey)) {
        analysisVal = analysisVal + jedis.get(analysisKey).toInt
      }
      jedis.setex(analysisKey, TimeSlotUtil.getRedisExSecond(slotType), String.valueOf(analysisVal))
      RedisClient.pool.returnResource(jedis)

      val timeSlotData = TimeSlotUtil.formatTimeSlot(hostReduceKey.timeSlot)

      (Map(Metadata.ID -> analysisKey), Map(
        "partition" -> partition,
        "host" -> hostReduceKey.host,
        "time_slot" -> hostReduceKey.timeSlot,
        "start_time" -> timeSlotData.startTime,
        "end_time" -> timeSlotData.endTime,
        "analysis_val" -> analysisVal,
        "create_date" -> periodTime))
    }).saveToEsWithMeta(Indicator_Index_Name + "_" + slotType + "_host_idx/" + indKeyName)
  }
}