package com.a.eye.gemini.analysis.util

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool

object RedisClient extends Serializable {
  val redisHost = "10.1.241.18"
  val redisPort = 6379
  val redisTimeout = 30000
  val password = "gemini"

  val config = new GenericObjectPoolConfig()
  config.setTestOnBorrow(true)
  config.setTestOnReturn(true)
  config.setMaxIdle(20)
  config.setMinIdle(10)
  config.setMaxTotal(1000)

  lazy val pool = new JedisPool(config, redisHost, redisPort, redisTimeout, password)

  lazy val hook = new Thread {
    override def run = {
      println("Execute hook thread: " + this)
      pool.destroy()
    }
  }
  sys.addShutdownHook(hook.run)
}