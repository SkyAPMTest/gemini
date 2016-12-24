package com.a.eye.gemini.analysis.util

import java.util.Calendar

import org.apache.logging.log4j.LogManager

import com.a.eye.gemini.analysis.config.GeminiConfig
import scala.runtime.RichInt

abstract class TimeSlotUtil extends Serializable {
  def compareSlotTime(tcpTime: Long): String
}

object TimeSlotUtil {
  val Atom = "atom"
  val Hour = "hour"
  val Day = "day"
  val Week = "week"
  val Month = "month"
  val Year = "year"
  val Time_Slot_Split = "-"

  def formatTimeSlot(timeSlot: String): TimeSlotData = {
    val timeSlots = timeSlot.split(Time_Slot_Split)
    val startTime = DateUtil.date2String(timeSlots.apply(0).toLong)
    val endTime = DateUtil.date2String(timeSlots.apply(1).toLong)
    new TimeSlotData(startTime, endTime)
  }

  def getRedisExSecond(slotType: String): Int = {
    if (Hour.equals(slotType)) {
      (60 * 60 * 1.2).toInt
    } else if (Day.equals(slotType)) {
      (60 * 60 * 24 * 1.2).toInt
    } else if (Week.equals(slotType)) {
      (60 * 60 * 24 * 7 * 1.2).toInt
    } else if (Month.equals(slotType)) {
      (60 * 60 * 24 * 31 * 1.2).toInt
    } else if (Year.equals(slotType)) {
      (60 * 60 * 24 * 366 * 1.2).toInt
    } else {
      60 * 20
    }
  }
}

class TimeSlotData(start: String, end: String) extends Serializable {
  var startTime: String = start
  var endTime: String = end
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

    slotStart.toString() + TimeSlotUtil.Time_Slot_Split + slotEnd.toString()
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

    slotStart.toString() + TimeSlotUtil.Time_Slot_Split + slotEnd.toString()
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

    slotStart.toString() + TimeSlotUtil.Time_Slot_Split + slotEnd.toString()
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

    slotStart.toString() + TimeSlotUtil.Time_Slot_Split + slotEnd.toString()
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

    slotStart.toString() + TimeSlotUtil.Time_Slot_Split + slotEnd.toString()
  }
}