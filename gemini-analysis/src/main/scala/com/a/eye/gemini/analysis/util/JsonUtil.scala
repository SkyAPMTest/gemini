package com.a.eye.gemini.analysis.util

import com.google.gson.JsonObject

object JsonUtil {
  def jsonObject2Map(jsonData: JsonObject): Map[String, String] = {
    val jsonIterator = jsonData.entrySet().iterator()
    var mapData: Map[String, String] = Map()
    while (jsonIterator.hasNext()) {
      val entry = jsonIterator.next()
      mapData = mapData ++ Map(entry.getKey -> entry.getValue.getAsString)
    }
    mapData
  }
}