package com.a.eye.gemini.analysis.executer.indicator

import com.a.eye.gemini.analysis.executer.GeminiAbstractExecuter
import org.apache.spark.rdd.RDD
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.a.eye.gemini.analysis.executer.model.IndicatorData
import org.elasticsearch.spark.rdd.Metadata
import com.a.eye.gemini.analysis.util.RedisClient

abstract class CommonIndicatorExecuter(indKey: String, indKeyName: String) extends GeminiAbstractExecuter {

  val Indicator_Index_Name = "gemini_" + indKeyName + "_ind"

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

  def saveIndicatorData(data: RDD[(IndicatorData)], partition: Int) {
    data.map(indicatorData => {
      (Map(Metadata.ID -> indicatorData.messageId), Map(
        "partition" -> partition,
        "seq" -> indicatorData.resSeq,
        "host" -> indicatorData.host,
        indicatorData.indKeyName -> indicatorData.indKey,
        "create_date" -> indicatorData.tcpTime))
    }).saveToEsWithMeta(Indicator_Index_Name + "_idx/" + indKeyName)
  }
}