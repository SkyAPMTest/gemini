package com.a.eye.gemini.analysis.util

object UrlUtil {
  def removeParameters(url: String): String = {
    val idx = url.indexOf("?")

    if (idx == -1) {
      url
    } else {
      url.substring(0, idx)
    }
  }
}