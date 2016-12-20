package com.a.eye.search.data.receive

import com.a.eye.gemini.analysis.executer.GeminiRegistry
import com.a.eye.gemini.analysis.executer.indicator.PvIndicatorExecuter
import com.a.eye.gemini.analysis.recevier.GeminiRecevier

object AnalysisStartUp {

  def main(args: Array[String]): Unit = {
    GeminiRegistry.register(new PvIndicatorExecuter)

    val recevier = new GeminiRecevier()
    recevier.startRecevie()
  }
}