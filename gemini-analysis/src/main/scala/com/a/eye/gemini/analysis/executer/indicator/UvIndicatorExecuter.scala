package com.a.eye.gemini.analysis.executer.indicator

import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
import org.elasticsearch.spark.rdd.Metadata
import org.elasticsearch.spark.sparkPairRDDFunctions

import com.a.eye.gemini.analysis.util.RedisClient
import com.google.gson.JsonObject

class UvIndicatorExecuter extends UniqueIndicatorExecuter("eth_source", "uv") {
}