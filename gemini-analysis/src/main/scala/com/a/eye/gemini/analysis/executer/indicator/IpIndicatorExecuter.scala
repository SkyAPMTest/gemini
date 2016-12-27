package com.a.eye.gemini.analysis.executer.indicator

import com.a.eye.gemini.analysis.executer.model.RecevierData
import com.google.gson.JsonObject

class IpIndicatorExecuter extends UniqueIndicatorExecuter("ip_source", false, "ip") {

  def validateReq(reqJson: JsonObject): Boolean = {
    reqJson.has("ip_source")
  }

  def validateRes(resJson: JsonObject): Boolean = {
    true
  }
}