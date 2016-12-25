package com.a.eye.gemini.analysis.util

import com.mongodb.casbah.MongoClient

object GeminiMongoClient {
  private val mongoClient = MongoClient("localhost", 27017)
  val db = mongoClient("gemini")
}