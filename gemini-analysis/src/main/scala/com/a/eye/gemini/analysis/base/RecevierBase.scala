package com.a.eye.gemini.analysis.base

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.CanCommitOffsets
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Assign
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.OffsetRange
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs.Ids

import com.a.eye.gemini.analysis.config.KafkaConfig
import com.a.eye.gemini.analysis.config.RedisConfig
import com.a.eye.gemini.analysis.config.SparkConfig
import com.a.eye.gemini.analysis.util.FakeWatcher
import com.a.eye.gemini.analysis.util.RedisClient
import com.a.eye.gemini.analysis.util.ZookeeperClient
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.typesafe.config.ConfigFactory
import org.apache.logging.log4j.LogManager
import kafka.message.MessageAndMetadata
import org.apache.spark.SparkException

abstract class RecevierBase(appName: String, topicName: String, groupId: String, esIdx: String, edType: String) extends Serializable {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  private var intervalTime = 10

  private val partition = "0"

  private val path = "/gemini/" + topicName + "/" + partition + "/offsets"

  private val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  val conf = ConfigFactory.load()

  def setIntervalTime(time: Integer) {
    this.intervalTime = time
  }

  private def initialize[K, V](): (InputDStream[ConsumerRecord[String, String]], StreamingContext) = {
    val sparkConf = new SparkConfig(conf).sparkConf.setAppName(appName).setMaster("local[1]")
    val streamingContext = new StreamingContext(sparkConf, Seconds(intervalTime))
    val redisClient = new RedisConfig(conf)
    val kafkaParams = new KafkaConfig(conf).kafkaParams + ("group.id" -> groupId) + ("consumer.id" -> "GeminiSparkConsumer")
    val topics = Array(topicName)

    val fromOffsets = selectOffsetsFromZookeeper.map { resultSet =>
      new TopicPartition(resultSet("topic"), resultSet("partition").toInt) -> resultSet("offset").toLong
    }.toMap

    (KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Assign[String, String](fromOffsets.keys.toList, kafkaParams, fromOffsets)), streamingContext)
  }

  def startRecevie() {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val init = initialize()
    val streamDS = init._1
    val streamingContext = init._2

    streamDS.foreachRDD(rdd => {
      val offsets = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      logger.info("本次处理的消息条数： " + rdd.count())
      offsets.foreach { x => logger.info("本次消息的偏移量：从" + x.fromOffset + " 到 " + x.untilOffset) }

      rdd.foreach(f => (println("请求key:" + f.key() + "------value:" + f.value())))

      val reqData = rdd.filter(record => !isResData(record)).map(record => { buildReqData(record) })
      val resData = rdd.filter(record => isResData(record)).map(record => { buildResData(record) })

      reqData.foreach(f => (println("请求" + f._1 + "------" + f._2)))

      val req_res_pairs = resData.map(response => {
        val resSeq = response._1
        val jedis = RedisClient.pool.getResource
        val request = jedis.get(resSeq);
        RedisClient.pool.returnResource(jedis)

        if (request != null) {
          val gson = new Gson()
          val reqJson = gson.fromJson(request, classOf[JsonObject])

          val pairs = new JsonObject();
          pairs.add("request", reqJson)
          pairs.add("response", response._2)

          (resSeq, pairs)
        } else {
          (resSeq, null)
        }
      })

      val timeStamp = new Date().getTime;
      //      resData.saveAsTextFile("file:///d:/data/res/" + timeStamp)
      //      reqData.saveAsTextFile("file:///d:/data/req/" + timeStamp)

      val count = req_res_pairs.filter(f => f._2 != null).count()
      logger.info("成功配对的数据条数：%d", count)

      //      req_res_pairs.filter(f => f._2 != null).saveAsTextFile("file:///d:/data/pairs/" + timeStamp)
      val pvData = req_res_pairs.filter(f => f._2 != null).map(f => {
        val host = f._2.getAsJsonObject("request").get("req_Host").getAsString
        val url = f._2.getAsJsonObject("request").get("req_RequestUrl").getAsString
        (host + url, 1)
      }).reduceByKey(_ + _)
      //      pvData.saveAsTextFile("file:///d:/data/pv/" + timeStamp)

      offsets.foreach { x => updateOffsetsToZookeeper(x.untilOffset) }
    })
    streamingContext.start()
    streamingContext.awaitTermination()
  }

  def isResData(record: ConsumerRecord[String, String]): Boolean

  def buildReqData(record: ConsumerRecord[String, String]): (String, JsonObject)

  def buildResData(record: ConsumerRecord[String, String]): (String, JsonObject)

  def selectOffsetsFromZookeeper(): Array[Map[String, String]] = {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val zkClient = new ZookeeperClient(conf, new FakeWatcher())
    initOffsets(path)

    val offsets = new String(zkClient.get(path))
    logger.info("起始的offsets位置为：%s", offsets)

    Array(Map("topic" -> topicName, "partition" -> partition, "offset" -> offsets))
  }

  def updateOffsetsToZookeeper(offsets: Long) {
    val zkClient = new ZookeeperClient(conf, new FakeWatcher())
    logger.info("更新zookeeper的offset，path=%s ,offsets=%d", path, offsets)
    zkClient.update(path, offsets.toString().getBytes)
  }

  def initOffsets(path: String) {
    val logger = LogManager.getFormatterLogger(this.getClass.getName)
    val zkClient = new ZookeeperClient(conf, new FakeWatcher())

    var pathTmp = ""
    path.split("/").filter { x => (x != null && x != "") }.foreach { node =>
      pathTmp += "/" + node
      logger.info("初始化zookeeper中的offsets path信息，path=%s", pathTmp)
      if (zkClient.notExists(pathTmp)) {
        logger.info("节点不存在，创建")
        zkClient.create(pathTmp, "0".getBytes, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
      } else {
        logger.info("节点存在，跳过")
      }
    }

    zkClient.close
  }
}