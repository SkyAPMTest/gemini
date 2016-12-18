package com.a.eye.gemini.analysis.executer

import org.apache.spark.streaming.StreamingContext

object GeminiAnalysis {

  private val executers = GeminiRegistry.executers

  def initializeEsData(streamingContext: StreamingContext) {
//    executers.foreach { executer => executer.detail() }
  }

  def startAnalysis() {
  }
}