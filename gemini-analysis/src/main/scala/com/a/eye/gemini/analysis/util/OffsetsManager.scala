package com.a.eye.gemini.analysis.util

import org.apache.logging.log4j.LogManager

import com.typesafe.config.ConfigFactory
import org.apache.zookeeper.ZooDefs.Ids
import org.apache.zookeeper.CreateMode
import breeze.util.partition

object OffsetsManager {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  val conf = ConfigFactory.load()

  def selectOffsets(topicName: String, partition: Int): Array[Map[String, String]] = {
    val zkClient = new ZookeeperClient(conf, new ZkWatcher())
    val path = getPath(topicName, partition).toString()
    initializeOffsets(path)

    val offsets = zkClient.get(path).mkString

    logger.info("起始的offsets位置为：%s", offsets)

    Array(Map("topic" -> topicName, "partition" -> partition.toString(), "offset" -> offsets))
  }

  def persistentOffsets(topicName: String, partition: Int, offsets: Long) {
    val zkClient = new ZookeeperClient(conf, new ZkWatcher())
    val path = getPath(topicName, partition)
    logger.info("更新zookeeper的offset，path=%s ,offsets=%d", path, offsets)
    zkClient.update(path.toString(), offsets.toString().getBytes)
  }

  private def initializeOffsets(path: String) {
    val zkClient = new ZookeeperClient(conf, new ZkWatcher())
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

  private def getPath(topicName: String, partition: Int) = {
    val path = "/gemini/" + topicName + "/" + partition.toString() + "/offsets"
  }
}