package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.util.RedisClient
import org.elasticsearch.spark.rdd.Metadata

abstract class UniqueIndicatorExecuter(indKey: String, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, indKeyName: String) {

  override def buildAnalysisAtomData(data: RDD[(IndicatorData)], partition: Int): RDD[(String, Int)] = {
    data.map(indicatorData => {
      val indKey = indicatorData.timeSlot + "|" + indicatorData.host + "|" + indicatorData.indKey
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
        jedis.setex(analysisKey, 120, String.valueOf(analysisVal))
      }
      RedisClient.pool.returnResource(jedis)
      (newKey, analysisVal)
    }).reduceByKey(_ + _)
  }

  override def saveAnalysisAtomData(data: RDD[(String, Int)], partition: Int) = {
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
        jedis.setex(analysisKey, 120, String.valueOf(analysisVal))
      }
      RedisClient.pool.returnResource(jedis)

      (Map(Metadata.ID -> analysisKey), Map(
        "partition" -> partition,
        "host" -> host,
        "timeSlot" -> timeSlot,
        indKeyName -> indKey,
        "analysisVal" -> analysisVal))
    }).saveToEsWithMeta(Indicator_Index_Name + "_atom_idx/" + indKeyName)
  }
}