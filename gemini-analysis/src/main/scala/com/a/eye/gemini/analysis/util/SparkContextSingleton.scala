package com.a.eye.gemini.analysis.util

import org.apache.spark.SparkContext

object SparkContextSingleton {
  var sparkContext: SparkContext = _
}