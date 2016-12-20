package com.a.eye.gemini.analysis.executer

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.rdd.RDD
import com.google.gson.JsonObject

object GeminiAnalysis {

  private val executers = GeminiRegistry.executers

  def initializeData(streamingContext: StreamingContext) {
    //    executers.foreach { executer => executer.detail() }
  }

  def startAnalysis(data: RDD[(Long, String, Long, JsonObject, String)], partition: Int) {
    executers.foreach { executer =>
      val indicatorData = executer.buildIndicatorData(data, partition)
      executer.saveIndicatorData(indicatorData, partition)
      val analysis = executer.buildAnalysisAtomData(indicatorData, partition)
      executer.saveAnalysisAtomData(analysis, partition)
    }
  }
}