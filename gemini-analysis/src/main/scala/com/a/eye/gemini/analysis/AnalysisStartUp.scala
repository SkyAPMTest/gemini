package com.a.eye.search.data.receive

import com.a.eye.gemini.analysis.executer.GeminiRegistry
import com.a.eye.gemini.analysis.executer.indicator.PvIndicatorExecuter
import com.a.eye.gemini.analysis.recevier.GeminiRecevier
import com.a.eye.gemini.analysis.executer.indicator.UvIndicatorExecuter
import com.a.eye.gemini.analysis.executer.indicator.IpIndicatorExecuter
import com.a.eye.gemini.analysis.executer.indicator.UvIndicatorExecuter

object AnalysisStartUp {

  def main(args: Array[String]): Unit = {
    GeminiRegistry.register(new PvIndicatorExecuter)
    GeminiRegistry.register(new UvIndicatorExecuter)
    GeminiRegistry.register(new IpIndicatorExecuter)

    val recevier = new GeminiRecevier()
    recevier.startRecevie()
  }
}