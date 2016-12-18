package com.a.eye.gemini.analysis.executer

import org.apache.spark.rdd.RDD

abstract class GeminiAbstractExecuter {

  def saveAtomPeriodEsData(data: RDD[(String, Int)], timestamp: Long)

  //  def initializeKeyInEs()

}