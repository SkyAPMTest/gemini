package com.a.eye.gemini.analysis.util

import org.apache.logging.log4j.LogManager

import com.typesafe.config.ConfigFactory
import org.apache.zookeeper.ZooDefs.Ids
import org.apache.zookeeper.CreateMode
import breeze.util.partition
import java.util.concurrent.CountDownLatch

object OffsetsManager {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  def selectOffsets(topicName: String, partition: Int): Array[Map[String, String]] = {
    val path = getPath(topicName, partition).toString()
    logger.info("起始的offsets在zk中的path为：%s", path)
    initializeOffsets(path)

    val offsets = new String(GeminiZkClient.get(path))

    logger.info("起始的offsets位置为：%s", offsets)

    Array(Map("topic" -> topicName, "partition" -> partition.toString(), "offset" -> offsets))
  }

  def persistentOffsets(topicName: String, partition: Int, offsets: Long) {
    val path = getPath(topicName, partition)
    logger.info("更新zookeeper的offset，path=%s ,offsets=%d", path, offsets)
    GeminiZkClient.update(path.toString(), offsets.toString().getBytes)
  }

  private def initializeOffsets(path: String) {
    var pathTmp = ""
    path.split("/").filter { x => (x != null && x != "") }.foreach { node =>
      pathTmp += "/" + node
      logger.info("初始化zookeeper中的offsets path信息，path=%s", pathTmp)
      if (GeminiZkClient.notExists(pathTmp)) {
        logger.info("节点不存在，创建")
        GeminiZkClient.create(pathTmp, "0".getBytes, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT)
      } else {
        logger.info("节点存在，跳过")
      }
    }
  }

  private def getPath(topicName: String, partition: Int): String = {
    "/gemini/" + topicName + "/" + partition.toString() + "/offsets"
  }
}