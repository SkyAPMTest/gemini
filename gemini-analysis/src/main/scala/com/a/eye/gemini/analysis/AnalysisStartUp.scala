package com.a.eye.search.data.receive

import com.a.eye.gemini.analysis.recevier.SnifferRecevier

object AnalysisStartUp {

  def main(args: Array[String]): Unit = {
    val intervalTime = args.apply(0)
    val sniffer = new SnifferRecevier()
    sniffer.setIntervalTime(Integer.parseInt(intervalTime))
    sniffer.startRecevie()
  }
}