package Stella

import Stella.Types.*

import scala.collection.mutable.Map

class FunctionsContext {
  val functionTypes: Map[String, FunctionType] = Map()
  def addFunction(funcName: String, funcType: FunctionType): Unit = {
    functionTypes += (funcName -> funcType)
  }
}