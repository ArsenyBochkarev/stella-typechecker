package Stella

import Stella.Types.*

import scala.collection.mutable.Map

class FunctionsContext {
  val functionTypes: Map[String, Type] = Map()
  val typeVars: Map[String, List[Type]] = Map()

  def addFunction(funcName: String, funcType: Type): Unit = {
    functionTypes += (funcName -> funcType)
  }
  def addTypeVariables(fn: String, typeList: List[Type]): Unit = typeVars += (fn -> typeList)
}