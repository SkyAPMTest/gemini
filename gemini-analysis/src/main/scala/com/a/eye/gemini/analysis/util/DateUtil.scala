package com.a.eye.gemini.analysis.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Calendar
import org.apache.logging.log4j.LogManager
import com.a.eye.gemini.analysis.config.GeminiConfig

object DateUtil {
  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  private val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  private var partitionTime = new Array[Long](64)

  def date2String(timeStamp: Long): String = {
    df.format(timeStamp)
  }
}