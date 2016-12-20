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

  def compareSlotTime(partition: Int, tcpTime: Long): String = {
    val calendar = Calendar.getInstance();
    calendar.setTimeInMillis(tcpTime)
    val second = calendar.get(Calendar.SECOND)
    logger.debug("秒：%s", second)

    val intervalTime = GeminiConfig.intervalTime
    val remainder = second % intervalTime
    logger.debug("余数：%d", remainder)

    val slotStart = tcpTime / 1000 / intervalTime
    val slotEnd = slotStart + 1

    logger.debug("时间：%s", df.format(new Date(slotStart * 1000 * intervalTime)))
    logger.debug("时间：%s", df.format(new Date(slotEnd * 1000 * intervalTime)))

    slotStart.toString() + "-" + slotEnd.toString()
  }
}