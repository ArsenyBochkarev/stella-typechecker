package Stella.Types

abstract class Type {
  override def toString: String = "Type"
  def replace(left: Type, right: Type): Type = this
}
