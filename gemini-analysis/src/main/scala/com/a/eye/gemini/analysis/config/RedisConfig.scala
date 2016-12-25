package com.a.eye.gemini.analysis.config

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object RedisConfig {
  
  private val conf = ConfigFactory.load()
  
  val redisParams = Map[String, Object](
    "redis.host" -> conf.getString("redis.host"),
    "redis.port" -> conf.getString("redis.port"),
    "redis.timeout" -> conf.getString("redis.timeout"))
}