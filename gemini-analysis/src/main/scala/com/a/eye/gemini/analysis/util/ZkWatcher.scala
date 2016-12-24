package com.a.eye.gemini.analysis.util

import org.apache.zookeeper.Watcher
import org.apache.zookeeper.WatchedEvent
import org.apache.zookeeper.Watcher.Event.KeeperState
import java.util.concurrent.CountDownLatch

class ZkWatcher(connectedLatch: CountDownLatch) extends Watcher {

  def process(event: WatchedEvent) {
    if (event.getState() == KeeperState.SyncConnected) {
      connectedLatch.countDown();
    }
  }
}