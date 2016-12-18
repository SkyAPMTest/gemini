package com.a.eye.gemini.analysis.executer

object GeminiRegistry {

  var executers: List[GeminiAbstractExecuter] = List[GeminiAbstractExecuter]()

  def register(clazz: GeminiAbstractExecuter) {
    executers = clazz :: executers
  }
}