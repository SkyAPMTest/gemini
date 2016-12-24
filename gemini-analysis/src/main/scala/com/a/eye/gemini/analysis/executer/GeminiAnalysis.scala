package com.a.eye.gemini.analysis.executer

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.rdd.RDD
import com.google.gson.JsonObject
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData

object GeminiAnalysis {

  private val executers = GeminiRegistry.executers

  def initializeData(streamingContext: StreamingContext) {
    //    executers.foreach { executer => executer.detail() }
  }

  def startAnalysis(data: RDD[(RecevierPairsData)], partition: Int, periodTime: String) {
    executers.foreach { executer =>
      val indicatorData = executer.buildIndicatorData(data, partition)
      executer.saveIndicatorData(indicatorData, partition, periodTime)

      executer.analysisAtomData(indicatorData, partition, periodTime)
      executer.analysisHourData(indicatorData, partition, periodTime)
      executer.analysisDayData(indicatorData, partition, periodTime)
      executer.analysisWeekData(indicatorData, partition, periodTime)
      executer.analysisMonthData(indicatorData, partition, periodTime)
    }
  }
}