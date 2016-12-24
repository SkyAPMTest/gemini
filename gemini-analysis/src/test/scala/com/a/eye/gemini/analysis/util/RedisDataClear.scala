package com.a.eye.gemini.analysis.util

import redis.clients.jedis.Jedis

object RedisDataClear {
  def main(args: Array[String]): Unit = {
    val jedis = RedisClient.pool.getResource

    deleteDay(jedis)
    RedisClient.pool.returnResource(jedis)
  }

  def deleteDay(jedis: Jedis) {
    val pre_str = "148"
    val set = jedis.keys(pre_str + "*")

    val it = set.iterator()
    while (it.hasNext()) {
      val keyStr = it.next()
      println(keyStr)
      jedis.del(keyStr)
    }
  }
}