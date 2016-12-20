package com.a.eye.gemini.analysis.executer.indicator

import com.a.eye.gemini.analysis.executer.GeminiAbstractExecuter
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.rdd.RDD
import org.elasticsearch.spark._
import com.google.gson.JsonObject
import org.elasticsearch.spark.rdd.Metadata
import com.a.eye.gemini.analysis.util.DateUtil
import com.a.eye.gemini.analysis.util.RedisClient
import org.apache.commons.lang3.StringUtils

class PvIndicatorExecuter extends GeminiAbstractExecuter {

  private val PvIndicator_Index_Name = "gemini_pv_ind_idx"

  private val PvAnalysis_Atom_Index_Name = "gemini_pv_atom_idx"

  private val PvIndicator_Type_Name = "pv"

  def buildIndicatorData(data: RDD[(Long, String, Long, JsonObject, String)], partition: Int): RDD[(Long, String, String, String, Long, String)] = {
    data.map(row => {
      val host = row._4.getAsJsonObject("request").get("req_Host").getAsString
      val url = row._4.getAsJsonObject("request").get("req_RequestUrl").getAsString
      val messageId = row._1
      val resSeq = row._2
      val tcpTime = row._3
      val timeSlot = row._5
      (messageId, resSeq, host, host + url, tcpTime, timeSlot)
    })
  }

  def saveIndicatorData(data: RDD[(Long, String, String, String, Long, String)], partition: Int) {
    data.map(pv => {
      (Map(Metadata.ID -> pv._1), Map(
        "partition" -> partition,
        "seq" -> pv._2,
        "host" -> pv._3,
        "link" -> pv._4,
        "slot_time" -> pv._6,
        "create_date" -> pv._5))
    }).saveToEsWithMeta(PvIndicator_Index_Name + "/" + PvIndicator_Type_Name)
  }

  def buildAnalysisAtomData(data: RDD[(Long, String, String, String, Long, String)], partition: Int): RDD[(String, Int)] = {
    data.map(row => {
      val key = row._4 + "|" + row._6
      (key, 1)
    }).reduceByKey(_ + _)
  }

  def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int) {
    data.map(pv => {
      val jedis = RedisClient.pool.getResource
      val idx = pv._1.indexOf("|")
      val link = pv._1.substring(0, idx)
      val timeSlot = pv._1.substring(idx + 1, pv._1.length())

      var pvInd = pv._2
      if (jedis.exists(pv._1)) {
        pvInd = pvInd + jedis.get(pv._1).toInt
      } else {
        jedis.setex(pv._1, 120, String.valueOf(pvInd))
      }
      RedisClient.pool.returnResource(jedis)

      (Map(Metadata.ID -> pv._1), Map(
        "partition" -> partition,
        "timeSlot" -> timeSlot,
        "link" -> link,
        "pv" -> pvInd))
    }).saveToEsWithMeta(PvAnalysis_Atom_Index_Name + "/" + PvIndicator_Type_Name)
  }

  def saveAnalysisHourData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisDayData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisWeekData(data: RDD[(String, Int)], partition: Int) {

  }

  def saveAnalysisMonthData(data: RDD[(String, Int)], partition: Int) {

  }
}