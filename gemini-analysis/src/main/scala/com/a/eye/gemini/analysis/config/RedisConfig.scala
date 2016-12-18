package com.a.eye.gemini.analysis.config

import com.typesafe.config.Config

class RedisConfig(conf: Config) {
  val redisParams = Map[String, Object](
    "redis.host" -> conf.getString("redis.host"),
    "redis.port" -> conf.getString("redis.port"),
    "redis.timeout" -> conf.getString("redis.timeout"))
}