package Stella.Types

object BoolType extends Type {
  override def toString: String = "Bool"

  override def isSubtypeOf(other: Type): Boolean =
    other match
      case BoolType => true
      case TopType => true
      case _ => false
}
