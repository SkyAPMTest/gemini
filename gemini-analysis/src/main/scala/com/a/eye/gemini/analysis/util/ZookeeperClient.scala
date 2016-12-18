package com.a.eye.gemini.analysis.util

import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.Watcher
import org.apache.zookeeper.ZooKeeper
import org.apache.zookeeper.data.ACL
import org.apache.zookeeper.data.Stat

import com.typesafe.config.Config

class ZookeeperClient(conf: Config, watcher: Watcher) {

  val sessionTimeout = conf.getInt("zk.session.timeout")
  val servers = conf.getString("zk.servers")

  private lazy val zk = new ZooKeeper(servers, sessionTimeout, watcher)

  def close {
    zk.close()
  }

  def get(path: String): Array[Byte] = {
    val stat: Stat = zk.exists(path, false)
    zk.getData(path, false, stat)
  }

  def getChildren(path: String): java.util.List[String] = {
    zk.getChildren(path, false)
  }

  def create(path: String, data: Array[Byte], acl: java.util.List[ACL], createMode: CreateMode): String = {
    zk.create(path, data, acl, createMode)
  }

  def delete(path: String) {
    zk.delete(path, -1)
  }

  def update(path: String, data: Array[Byte]) {
    zk.setData(path, data, -1)
  }

  def notExists(path: String): Boolean = {
    val stat = zk.exists(path, false)
    if (stat == null) {
      true
    } else {
      false
    }
  }

  def isAlive: Boolean = {
    val result: Stat = zk.exists("/", false) // do not watch

    if (result.getVersion >= 0) {
      true
    } else {
      false
    }
  }
}