package Stella

import scala.collection.mutable.Map
import Stella.Types._

class VarContext {
  val varTypes: Map[String, Type] = Map()
  def addVariable(variable: Variable): Unit = {
    varTypes += (variable.str -> variable.varType)
  }
}
