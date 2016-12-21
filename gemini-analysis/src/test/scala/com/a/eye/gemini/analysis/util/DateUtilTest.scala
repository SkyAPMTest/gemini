package com.a.eye.gemini.analysis.util

import java.util.Date

object DateUtilTest {
  def main(args: Array[String]): Unit = {
    val partition = 0
    val tcpTime = new Date().getTime

    val atom = new AtomTimeSlotUtil().compareSlotTime(tcpTime)
    println(atom)
    val atoms = atom.split("-")
    println(DateUtil.date2String(atoms.apply(0).toLong) + " - " + DateUtil.date2String(atoms.apply(1).toLong))

    val hour = new HourTimeSlotUtil().compareSlotTime(tcpTime)
    println(hour)
    val hours = hour.split("-")
    println(DateUtil.date2String(hours.apply(0).toLong) + " - " + DateUtil.date2String(hours.apply(1).toLong))

    val day = new DayTimeSlotUtil().compareSlotTime(tcpTime)
    println(day)
    val days = day.split("-")
    println(DateUtil.date2String(days.apply(0).toLong) + " - " + DateUtil.date2String(days.apply(1).toLong))

    val week = new WeekTimeSlotUtil().compareSlotTime(tcpTime)
    println(week)
    val weeks = week.split("-")
    println(DateUtil.date2String(weeks.apply(0).toLong) + " - " + DateUtil.date2String(weeks.apply(1).toLong))

    val month = new MonthTimeSlotUtil().compareSlotTime(tcpTime)
    println(month)
    val months = month.split("-")
    println(DateUtil.date2String(months.apply(0).toLong) + " - " + DateUtil.date2String(months.apply(1).toLong))
  }
}