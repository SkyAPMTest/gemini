package com.a.eye.gemini.analysis.base

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.log4j.LogManager
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.CanCommitOffsets
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.DirectKafkaInputDStream
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.elasticsearch.spark._
import com.typesafe.config.ConfigFactory
import org.elasticsearch.spark.rdd.Metadata
import java.io.PrintWriter
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import org.elasticsearch.spark.rdd.EsSpark
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.a.eye.gemini.analysis.config.SparkConfig
import com.a.eye.gemini.analysis.config.KafkaConfig
import org.elasticsearch.spark.rdd.api.java.JavaEsSpark

abstract class RecevierBase(appName: String, topicName: String, groupId: String, esIdx: String, edType: String) extends Serializable {

  private var intervalTime = 10;

  private val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  val conf = ConfigFactory.load()

  def setIntervalTime(time: Integer) {
    this.intervalTime = time
  }

  private def initialize[K, V](): (InputDStream[ConsumerRecord[String, String]], StreamingContext) = {
    val sparkConf = new SparkConfig(conf).sparkConfig.setAppName(appName).setMaster("local")
    val streamingContext = new StreamingContext(sparkConf, Seconds(intervalTime))
    val kafkaParams = new KafkaConfig(conf).kafkaParams + ("group.id" -> groupId)
    val topics = Array(topicName)
    (KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](topics, kafkaParams)), streamingContext)
  }

  def startRecevie() {
    val logger = LogManager.getLogger(this.getClass.getName)
    val init = initialize()
    val streamDS = init._1
    val streamingContext = init._2
    streamDS.foreachRDD(rdd => {
      val offsets = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      logger.info("本次处理的消息条数： " + rdd.count())
      offsets.foreach { x => logger.info("本次消息的偏移量：从" + x.fromOffset + " 到 " + x.untilOffset) }
      if (!rdd.isEmpty()) {
        rdd.filter(record => validateData(record)).map(record => { buildEsData(record) }).saveToEsWithMeta(esIdx + "/" + edType)
        rdd.filter(record => !validateData(record)).map(record => { buildErrorEsData(record) }).saveToEsWithMeta("error_" + esIdx + "/" + edType)
      }
      streamDS.asInstanceOf[CanCommitOffsets].commitAsync(offsets)
    })
    streamingContext.start()
    streamingContext.awaitTermination()
  }

  def validateData(record: ConsumerRecord[String, String]): Boolean

  def buildEsData(record: ConsumerRecord[String, String]): (Map[Metadata, String], Map[String, String])

  def buildErrorEsData(record: ConsumerRecord[String, String]): (Map[Metadata, String], Map[String, String]) = {
    (Map(Metadata.ID -> record.key()), Map(
      "data" -> record.value(),
      "create_date" -> df.format(new Date())))
  }
}