package com.a.eye.gemini.analysis.executer.model

import com.google.gson.JsonObject

class RecevierPairsData extends Serializable {
  var messageId: Long = _
  var seq: String = _
  var tcpTime: Long = _
  var pairs: JsonObject = _
}