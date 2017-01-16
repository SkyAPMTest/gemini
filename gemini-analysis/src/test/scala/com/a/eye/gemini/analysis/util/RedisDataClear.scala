package com.a.eye.gemini.analysis.util

import redis.clients.jedis.Jedis

object RedisDataClear {
  def main(args: Array[String]): Unit = {
    deleteDay(RedisClient.jedis)
  }

  def deleteDay(jedis: Jedis) {
    val pre_str = ""
    val set = jedis.keys(pre_str + "*")

    jedis.del(pre_str + "*")

    jedis.flushDB()

    //    val it = set.iterator()
    //    while (it.hasNext()) {
    //      val keyStr = it.next()
    ////      println(keyStr)
    //      jedis.del(keyStr)
    //    }
  }
}