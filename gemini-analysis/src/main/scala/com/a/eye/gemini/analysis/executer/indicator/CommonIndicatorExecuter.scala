package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrameWriter
import org.apache.spark.sql.SQLContext
import org.bson.Document

import com.a.eye.gemini.analysis.config.SparkConfig
import com.a.eye.gemini.analysis.executer.GeminiAbstractExecuter
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.util.AtomTimeSlotUtil
import com.a.eye.gemini.analysis.util.DayTimeSlotUtil
import com.a.eye.gemini.analysis.util.HourTimeSlotUtil
import com.a.eye.gemini.analysis.util.MonthTimeSlotUtil
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.ReduceKeyUtil
import com.a.eye.gemini.analysis.util.SparkContextSingleton
import com.a.eye.gemini.analysis.util.TimeSlotUtil
import com.a.eye.gemini.analysis.util.WeekTimeSlotUtil
import com.mongodb.casbah.commons.MongoDBObject
import com.a.eye.gemini.analysis.util.GeminiMongoClient
import org.apache.logging.log4j.LogManager

abstract class CommonIndicatorExecuter(indKey: String, isUseIndValue: Boolean, keyInDbName: String) extends GeminiAbstractExecuter {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def analysisAtomData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new AtomTimeSlotUtil(), keyInDbName, TimeSlotUtil.Atom)
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Atom, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Atom)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Atom, periodTime)
  }

  override def analysisHourData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new HourTimeSlotUtil(), keyInDbName, TimeSlotUtil.Hour)
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Hour, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Hour)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Hour, periodTime)
  }

  override def analysisDayData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new DayTimeSlotUtil(), keyInDbName, TimeSlotUtil.Day)
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Day, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Day)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Day, periodTime)
  }

  override def analysisWeekData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new WeekTimeSlotUtil(), keyInDbName, TimeSlotUtil.Week)
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Week, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Week)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Week, periodTime)
  }

  override def analysisMonthData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    val indiSlotData = this.buildAnalysisIndiSlotData(data, partition, new MonthTimeSlotUtil(), keyInDbName, TimeSlotUtil.Month)
    this.saveAnalysisIndiData(indiSlotData, partition, TimeSlotUtil.Month, periodTime)
    val hostSlotData = this.buildAnalysisHostSlotData(indiSlotData, TimeSlotUtil.Month)
    this.saveAnalysisHostData(hostSlotData, partition, TimeSlotUtil.Month, periodTime)
  }

  override def buildIndicatorData(data: Array[(RecevierPairsData)], partition: Int): RDD[(IndicatorData)] = {
    val dataArray = data.map(recevierPairsData => {
      val indicatorData = new IndicatorData()
      indicatorData.messageId = recevierPairsData.messageId
      indicatorData.resSeq = recevierPairsData.tcpSeq
      indicatorData.host = recevierPairsData.reqData.get("req_Host").get
      indicatorData.indKey = recevierPairsData.reqData.get(indKey).get
      indicatorData.tcpTime = recevierPairsData.tcpTime
      logger.debug("统计值：%s", indicatorData.indKey)
      (indicatorData)
    })

    SparkContextSingleton.sparkContext.parallelize(dataArray)
  }

  override def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int, periodTime: String) {
    data.collect().foreach(indicatorData => {
      var mongoData = MongoDBObject("_id" -> indicatorData.messageId,
        "partition" -> partition,
        "seq" -> indicatorData.resSeq,
        "host" -> indicatorData.host,
        "key" -> indicatorData.indKey,
        "tcp_time" -> indicatorData.tcpTime,
        "create_date" -> periodTime)

      GeminiMongoClient.db("indicator_" + keyInDbName).insert(mongoData)
    })
  }

  override def saveAnalysisIndiData(data: RDD[(String, Long)], partition: Int, slotType: String, periodTime: String) {
    data.collect().foreach(analysisRow => {
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

      var mongoData = MongoDBObject("_id" -> analysisKey,
        "partition" -> partition,
        "host" -> indiReduceKey.host,
        "time_slot" -> indiReduceKey.timeSlot,
        "start_time" -> timeSlotData.startTime,
        "end_time" -> timeSlotData.endTime,
        "key" -> indiReduceKey.indKey,
        "value" -> analysisVal,
        "create_date" -> periodTime)

      val collection = "indicator_" + slotType + "_" + keyInDbName
      GeminiMongoClient.db(collection).remove(MongoDBObject("_id" -> analysisKey))
      GeminiMongoClient.db(collection).insert(mongoData)
    })
  }

  override def saveAnalysisHostData(data: RDD[(String, Long)], partition: Int, slotType: String, periodTime: String) {
    data.collect().foreach(analysisRow => {
      val jedis = RedisClient.pool.getResource
      val analysisKey = analysisRow._1
      var analysisVal = analysisRow._2

      var hostReduceKey = ReduceKeyUtil.parseHostReduceKey(analysisKey)

      if (jedis.exists(analysisKey)) {
        analysisVal = analysisVal + jedis.get(analysisKey).toInt
      }
      jedis.setex(analysisKey, TimeSlotUtil.getRedisExSecond(slotType), String.valueOf(analysisVal))
      RedisClient.pool.returnResource(jedis)

      val timeSlotData = TimeSlotUtil.formatTimeSlot(hostReduceKey.timeSlot)

      var host = hostReduceKey.host
      if (isUseIndValue) {
        val firstIdx = host.indexOf(ReduceKeyUtil.Split_Str)
        host = host.substring(0, firstIdx)
      }

      var mongoData = MongoDBObject("_id" -> analysisKey,
        "partition" -> partition,
        "host" -> host,
        "time_slot" -> hostReduceKey.timeSlot,
        "start_time" -> timeSlotData.startTime,
        "end_time" -> timeSlotData.endTime,
        "value" -> analysisVal,
        "create_date" -> periodTime)

      val collection = "indicator_host_" + slotType + "_" + keyInDbName
      GeminiMongoClient.db(collection).remove(MongoDBObject("_id" -> analysisKey))
      GeminiMongoClient.db(collection).insert(mongoData)
    })
  }
}