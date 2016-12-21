package com.a.eye.gemini.analysis.util

object ReduceKeyUtil {

  private val Split_Str = "|"

  def buildIndiReduceKey(timeSlot: String, host: String, indKey: String): String = {
    timeSlot + Split_Str + host + Split_Str + indKey
  }

  def parseIndiReduceKey(indiReduceKeyStr: String): IndiReduceKey = {
    val firstIdx = indiReduceKeyStr.indexOf("|")
    val secondIdx = indiReduceKeyStr.indexOf("|", firstIdx + 1)

    val indiReduceKey = new IndiReduceKey()

    indiReduceKey.timeSlot = indiReduceKeyStr.substring(0, firstIdx)
    indiReduceKey.host = indiReduceKeyStr.substring(firstIdx + 1, secondIdx)
    indiReduceKey.indKey = indiReduceKeyStr.substring(secondIdx + 1, indiReduceKeyStr.length())

    indiReduceKey
  }

  def parseIndiKeyToHostKey(indiReduceKey: String): String = {
    val firstIdx = indiReduceKey.indexOf("|")
    val secondIdx = indiReduceKey.indexOf("|", firstIdx + 1)

    indiReduceKey.substring(0, secondIdx)
  }

  def parseHostReduceKey(hostReduceKeyStr: String): HostReduceKey = {
    val firstIdx = hostReduceKeyStr.indexOf("|")

    val hostReduceKey = new HostReduceKey()

    hostReduceKey.timeSlot = hostReduceKeyStr.substring(0, firstIdx)
    hostReduceKey.host = hostReduceKeyStr.substring(firstIdx + 1, hostReduceKeyStr.length())

    hostReduceKey
  }

  class IndiReduceKey {
    var timeSlot: String = _
    var host: String = _
    var indKey: String = _
  }

  class HostReduceKey {
    var timeSlot: String = _
    var host: String = _
  }
}