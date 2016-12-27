package com.a.eye.gemini.analysis.executer

import org.apache.logging.log4j.LogManager
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext

import com.a.eye.gemini.analysis.executer.model.RecevierData
import com.a.eye.gemini.analysis.executer.model.RecevierPairsData
import com.google.gson.JsonObject

object GeminiAnalysis {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  private val executers = GeminiRegistry.executers

  def initializeData(streamingContext: StreamingContext) {
    //    executers.foreach { executer => executer.detail() }
  }

  def startAnalysis(data: Array[(RecevierPairsData)], partition: Int, periodTime: String) {
    executers.foreach { executer =>
      logger.info("构建并保存明细数据 -- 开始")
      val indicatorData = executer.buildIndicatorData(data, partition)
      executer.saveIndicatorData(indicatorData, partition, periodTime)
      logger.info("构建并保存明细数据 -- 完毕")

      executer.analysisAtomData(indicatorData, partition, periodTime)
      logger.info("构建并保存一个周期数据（原子）, 指标：%s -- 完毕", executer.getClass.getSimpleName)
      executer.analysisHourData(indicatorData, partition, periodTime)
      logger.info("构建并保存一个周期数据（小时）, 指标：%s  -- 完毕", executer.getClass.getSimpleName)
      executer.analysisDayData(indicatorData, partition, periodTime)
      logger.info("构建并保存一个周期数据（天）指标：%s -- 完毕", executer.getClass.getSimpleName)
      executer.analysisWeekData(indicatorData, partition, periodTime)
      logger.info("构建并保存一个周期数据（周）指标：%s -- 完毕", executer.getClass.getSimpleName)
      executer.analysisMonthData(indicatorData, partition, periodTime)
      logger.info("构建并保存一个周期数据（月）指标：%s -- 完毕", executer.getClass.getSimpleName)
    }
  }

  def validateReq(reqJson: JsonObject): Boolean = {
    var pass = true
    executers.foreach { executer =>
      pass = pass && executer.validateReq(reqJson)
    }
    pass
  }

  def validateRes(resJson: JsonObject): Boolean = {
    var pass = true
    executers.foreach { executer =>
      pass = pass && executer.validateRes(resJson)
    }
    pass
  }
}