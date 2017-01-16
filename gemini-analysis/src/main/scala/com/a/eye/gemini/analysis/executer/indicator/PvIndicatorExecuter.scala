package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import com.a.eye.gemini.analysis.util.UrlUtil

class PvIndicatorExecuter extends CumulativeIndicatorExecuter("req_requesturl", false, "pv") {

  def filterIndicatorData(data: RDD[Map[String, String]], partition: Int): RDD[Map[String, String]] = {
    //    data.filter(filterRecord => {
    //      filterRecord.contains("req_host") && filterRecord.get("req_host").get.equals("home.asiainfo.com")
    //    }).

    data.map(record => {
      var newRecord = record
      if (record.contains("req_requesturl")) {
        //        val url = UrlUtil.removeParameters(record.get("req_requesturl").get)
        val url = "/favicon.ico"
        newRecord += ("req_requesturl" -> (record.get("req_host").get + url))
      } else {
        newRecord += ("req_requesturl" -> (record.get("req_host").get))
      }
      newRecord += ("req_host" -> "home.asiainfo.com")
      newRecord
    })
  }
}