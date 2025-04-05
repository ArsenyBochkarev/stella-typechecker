package Stella.Types

abstract class Type {
  override def toString: String = "Type"
  def isSubtypeOf(other: Type): Boolean
}
