package com.a.eye.gemini.analysis.executer.model

import com.google.gson.JsonObject

class RecevierPairsData extends Serializable {
  var messageId: Long = _
  var tcpSeq: String = _
  var tcpAck: String = _
  var tcpTime: Long = _
  var reqData: Map[String, String] = _
  var resData: Map[String, String] = _
  var isPair: Boolean = _
}