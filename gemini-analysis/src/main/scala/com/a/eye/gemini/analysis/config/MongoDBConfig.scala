package com.a.eye.gemini.analysis.config

import com.typesafe.config.ConfigFactory

object MongoDBConfig {
  private val conf = ConfigFactory.load()
  lazy val uri = conf.getString("spark.mongodb.output.uri") + conf.getString("spark.mongodb.output.database")
}