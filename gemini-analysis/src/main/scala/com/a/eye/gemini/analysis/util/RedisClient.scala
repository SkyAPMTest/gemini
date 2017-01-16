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

  private lazy val pool = new JedisPool(config, redisHost, redisPort, redisTimeout, password)

  lazy val jedis = RedisClient.pool.getResource

  def exists(keys: String): Boolean = {
    return jedis.exists(keys);
  }

  def get(key: String): String = {
    return jedis.get(key)
  }

  def setex(key: String, seconds: Int, value: String) {
    jedis.setex(key, seconds, value)
  }

  lazy val hook = new Thread {
    override def run = {
      println("Execute hook thread: " + this)
      pool.destroy()
    }
  }
  sys.addShutdownHook(hook.run)
}