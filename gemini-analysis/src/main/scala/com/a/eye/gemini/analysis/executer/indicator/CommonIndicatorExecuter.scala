package com.a.eye.gemini.analysis.executer.indicator

import com.a.eye.gemini.analysis.executer.GeminiAbstractExecuter
import org.apache.spark.rdd.RDD
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import org.elasticsearch.spark.rdd.Metadata
import com.a.eye.gemini.analysis.util.RedisClient

abstract class CommonIndicatorExecuter(indKey: String, indKeyName: String) extends GeminiAbstractExecuter {

  private val Indicator_Index_Name = "gemini_" + indKeyName + "_ind"

  def buildIndicatorData(data: RDD[(RecevierPairsData)], partition: Int): RDD[(IndicatorData)] = {
    data.map(recevierPairsData => {
      val indicatorData = new IndicatorData()
      indicatorData.messageId = recevierPairsData.messageId
      indicatorData.resSeq = recevierPairsData.seq
      indicatorData.host = recevierPairsData.pairs.getAsJsonObject("request").get("req_Host").getAsString
      indicatorData.indKey = recevierPairsData.pairs.getAsJsonObject("request").get(indKey).getAsString
      indicatorData.indKeyName = indKeyName
      indicatorData.tcpTime = recevierPairsData.tcpTime
      indicatorData.timeSlot = recevierPairsData.slotTime
      (indicatorData)
    })
  }

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int) {
    data.map(indicatorData => {
      (Map(Metadata.ID -> indicatorData.host), Map(
        "partition" -> partition,
        "seq" -> indicatorData.resSeq,
        "host" -> indicatorData.host,
        indicatorData.indKeyName -> indicatorData.indKey,
        "slot_time" -> indicatorData.timeSlot,
        "create_date" -> indicatorData.tcpTime))
    }).saveToEsWithMeta(Indicator_Index_Name + "_idx/" + indKeyName)
  }

  def buildAnalysisAtomData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    data.map(indicatorData => {
      val indKey = indicatorData.timeSlot + "|" + indicatorData.host + "|" + indicatorData.indKey
      (indKey, 1)
    }).reduceByKey(_ + _)
  }

  def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int) {
    data.map(ip => {
      val jedis = RedisClient.pool.getResource
      val idx = ip._1.indexOf("|")
      val timeSlot = ip._1.substring(0, idx)
      val iphost = ip._1.substring(idx + 1, ip._1.length())
      val ipIdx = iphost.indexOf("|")
      val ipAddr = iphost.substring(0, ipIdx)
      val host = iphost.substring(ipIdx + 1, iphost.length())

      var ipInd = ip._2
      if (jedis.exists(ip._1)) {
        ipInd = ipInd + jedis.get(ip._1).toInt
      } else {
        jedis.setex(ip._1, 120, String.valueOf(ipInd))
      }
      RedisClient.pool.returnResource(jedis)

      (Map(Metadata.ID -> ip._1), Map(
        "partition" -> partition,
        "host" -> host,
        "timeSlot" -> timeSlot,
        "ip" -> ipAddr,
        "ip" -> ipInd))
    }).saveToEsWithMeta(Indicator_Index_Name + "_atom_idx/" + indKeyName)
  }
}