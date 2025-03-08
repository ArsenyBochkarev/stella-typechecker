package Stella.Error

import scala.collection.mutable

object ErrorManager {
  private var errorQueue: mutable.Queue[StellaError] = new mutable.Queue[StellaError]()

  def registerError(err: StellaError): Unit = {
    errorQueue.enqueue(err)
  }
  def outputErrors(): Unit = {
    for (e <- errorQueue)
      System.err.println(s"Error: ${e.errorText}")
  }
}
