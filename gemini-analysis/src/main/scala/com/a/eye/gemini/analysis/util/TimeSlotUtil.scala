package com.a.eye.gemini.analysis.util

import java.util.Calendar
import com.a.eye.gemini.analysis.config.GeminiConfig
import org.apache.logging.log4j.LogManager

abstract class TimeSlotUtil extends Serializable {
  def compareSlotTime(tcpTime: Long): String
}

object TimeSlotUtil {
  val Atom = "atom"
  val Hour = "hour"
  val Day = "day"
  val Week = "week"
  val Month = "month"
}

class AtomTimeSlotUtil extends TimeSlotUtil {
  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def compareSlotTime(tcpTime: Long): String = {
    val calendar = Calendar.getInstance();
    calendar.setTimeInMillis(tcpTime)
    val second = calendar.get(Calendar.SECOND)
    logger.debug("秒：%s", second)

    val intervalTime = GeminiConfig.intervalTime
    val remainder = second % intervalTime
    logger.debug("余数：%d", remainder)

    val ratio = 1000 * intervalTime
    val slotStart = (tcpTime / ratio) * ratio
    val slotEnd = (tcpTime / ratio + 1) * ratio

    slotStart.toString() + "-" + slotEnd.toString()
  }
}

class HourTimeSlotUtil extends TimeSlotUtil {
  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def compareSlotTime(tcpTime: Long): String = {
    val calendar = Calendar.getInstance();
    calendar.setTimeInMillis(tcpTime)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val slotStart = calendar.getTimeInMillis

    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    val slotEnd = calendar.getTimeInMillis

    slotStart.toString() + "-" + slotEnd.toString()
  }
}

class DayTimeSlotUtil extends TimeSlotUtil {
  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def compareSlotTime(tcpTime: Long): String = {
    val calendar = Calendar.getInstance();
    calendar.setTimeInMillis(tcpTime)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val slotStart = calendar.getTimeInMillis

    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    val slotEnd = calendar.getTimeInMillis

    slotStart.toString() + "-" + slotEnd.toString()
  }
}

class WeekTimeSlotUtil extends TimeSlotUtil {
  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def compareSlotTime(tcpTime: Long): String = {
    val calendar = Calendar.getInstance();
    calendar.setTimeInMillis(tcpTime)
    calendar.setFirstDayOfWeek(Calendar.MONDAY)
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val slotStart = calendar.getTimeInMillis

    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    val slotEnd = calendar.getTimeInMillis

    slotStart.toString() + "-" + slotEnd.toString()
  }
}

class MonthTimeSlotUtil extends TimeSlotUtil {
  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  override def compareSlotTime(tcpTime: Long): String = {
    val calendar = Calendar.getInstance();
    calendar.setTimeInMillis(tcpTime)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    val slotStart = calendar.getTimeInMillis

    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    val slotEnd = calendar.getTimeInMillis

    slotStart.toString() + "-" + slotEnd.toString()
  }
}