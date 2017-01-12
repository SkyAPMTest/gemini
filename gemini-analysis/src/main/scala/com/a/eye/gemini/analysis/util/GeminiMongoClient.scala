package com.a.eye.gemini.analysis.util

import com.mongodb.casbah.MongoClient

object GeminiMongoClient {
  private val mongoClient = MongoClient("10.1.241.18", 27017)
  val db = mongoClient("gemini")
}