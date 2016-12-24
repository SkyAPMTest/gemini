package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD

import com.a.eye.gemini.analysis.executer.model.IndicatorData
import com.a.eye.gemini.analysis.util.AtomTimeSlotUtil
import com.a.eye.gemini.analysis.util.DayTimeSlotUtil
import com.a.eye.gemini.analysis.util.HourTimeSlotUtil
import com.a.eye.gemini.analysis.util.MonthTimeSlotUtil
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.ReduceKeyUtil
import com.a.eye.gemini.analysis.util.WeekTimeSlotUtil

abstract class CumulativeIndicatorExecuter(indKey: String, indKeyName: String) extends CommonIndicatorExecuter(indKey: String, indKeyName: String) {

  override def buildAnalysisHostSlotData(data: RDD[(String, Int)], slotType: String): RDD[(String, Int)] = {
    data.map(analysisIndiSlotData => {
      val analysisKey = analysisIndiSlotData._1
      val analysisVal = analysisIndiSlotData._2

      val hostKey = ReduceKeyUtil.parseIndiKeyToHostKey(analysisKey)

      (hostKey, analysisVal)
    }).reduceByKey(_ + _)
  }
}