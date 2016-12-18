package com.a.eye.gemini.analysis.executer.indicator

import com.a.eye.gemini.analysis.executer.GeminiAbstractExecuter
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.rdd.RDD
import org.elasticsearch.spark._

class PvIndicatorExecuter extends GeminiAbstractExecuter {

  private val PvIndicator_Index_Name = "gemini_pv_idx"

  private val PvIndicator_Type_Name = "indicator"

  private val PvIndicator_Es = PvIndicator_Index_Name + "/" + PvIndicator_Type_Name

  def saveAtomPeriodEsData(data: RDD[(String, Int)], timestamp: Long) {
    data.map(f => {
      Map(
        "timestamp" -> timestamp,
        "url" -> f._1,
        "pv" -> f._2)
    }).saveToEs(PvIndicator_Es)
  }

  def initEsData(streamingContext: StreamingContext) {
    val rdd = streamingContext.sparkContext.esRDD(PvIndicator_Es)
    rdd.foreach(f => {
      println("row url=" + f._2.get("url").get + ", pv=" + f._2.get("pv").get.toString().toInt)
      (f._2.get("url").get, f._2.get("pv").get.toString().toInt)
    })
  }

  def saveEsData(pv: RDD[(String, Int)]) {
    pv.map(f => {
      Map(
        "url" -> f._1,
        "pv" -> f._2)
    }).saveToEs("gemini_idx/analysis")
  }
}