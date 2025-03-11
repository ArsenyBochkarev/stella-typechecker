package Stella

object OutputManager {
  private var result: Int = 0
  def getOutput: Int = result
  def addError(): Unit = result = 1
}
