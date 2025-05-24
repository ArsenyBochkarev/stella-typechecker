package Stella.Error

import Stella.OutputManager

import scala.collection.mutable

object ErrorManager {
  var errorQueue: mutable.Queue[StellaError] = new mutable.Queue[StellaError]()

  def registerError(err: StellaError): Unit = {
    errorQueue.enqueue(err)
  }
  def outputErrors(): Unit = {
    if errorQueue.nonEmpty then OutputManager.addError()
    for (e <- errorQueue)
      System.err.println(s"${e.getClass.getSimpleName}: ${e.errorText}")
  }
}
