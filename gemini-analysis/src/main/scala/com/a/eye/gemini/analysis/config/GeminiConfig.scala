package com.a.eye.gemini.analysis.config

import com.typesafe.config.ConfigFactory

object GeminiConfig {

  val conf = ConfigFactory.load()
  lazy val intervalTime = conf.getInt("gemini.intervalTime")
}