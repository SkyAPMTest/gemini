package com.a.eye.search.data.receive

import com.a.eye.gemini.analysis.executer.GeminiRegistry
import com.a.eye.gemini.analysis.executer.indicator.PvIndicatorExecuter
import com.a.eye.gemini.analysis.recevier.GeminiSnifferRecevier

object AnalysisStartUp {

  def main(args: Array[String]): Unit = {
    val intervalTime = args.apply(0)

    GeminiRegistry.register(new PvIndicatorExecuter)

    val recevier = new GeminiSnifferRecevier()
    recevier.setIntervalTime(Integer.parseInt(intervalTime))
    recevier.startRecevie()
  }
}