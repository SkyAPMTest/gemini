package com.a.eye.gemini.analysis.util

import org.apache.logging.log4j.LogManager
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.ZooDefs.Ids

object OffsetsManager {

  private val logger = LogManager.getFormatterLogger(this.getClass.getName)

  def selectOffsets(topicName: String, partitions: Int): Array[Map[String, String]] = {
    var offsets: Array[Map[String, String]] = new Array[Map[String, String]](partitions)

    for (partition <- 0 to partitions - 1) {
      val path = getPath(topicName, partition)
      logger.info("partition：%d 的起始offsets在zk中的path为：%s", partition, path)
      initializeOffsets(path)
      val offset = new String(GeminiZkClient.get(path))
      logger.info("partition：%d 的起始offsets位置为：%s", partition, offset)
      offsets(partition) = Map("topic" -> topicName, "partition" -> partition.toString(), "offset" -> offset)
    }

    offsets
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