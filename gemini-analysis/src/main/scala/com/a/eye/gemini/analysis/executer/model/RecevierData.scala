package com.a.eye.gemini.analysis.executer.model

class RecevierData extends Serializable {
  var messageId: Long = _
  var tcpSeq: String = _
  var tcpAck: String = _
  var tcpTime: Long = _
  var data: Map[String, String] = _
}