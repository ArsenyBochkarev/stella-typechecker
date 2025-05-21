package Stella

import Stella.Types.*

import scala.collection.mutable.Map

class FunctionsContext {
  val functionTypes: Map[String, Type] = Map()
  def addFunction(funcName: String, funcType: Type): Unit = {
    functionTypes += (funcName -> funcType)
  }
}