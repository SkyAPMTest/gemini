package com.a.eye.gemini.analysis.util

import org.apache.commons.pool2.impl.GenericObjectPoolConfig
import redis.clients.jedis.JedisPool

object RedisClient extends Serializable {
  val redisHost = "10.19.7.241"
  val redisPort = 31709
  val redisTimeout = 30000
  val password = "oa_redis"
  lazy val pool = new JedisPool(new GenericObjectPoolConfig(), redisHost, redisPort, redisTimeout, password)

  lazy val hook = new Thread {
    override def run = {
      println("Execute hook thread: " + this)
      pool.destroy()
    }
  }
  sys.addShutdownHook(hook.run)
}