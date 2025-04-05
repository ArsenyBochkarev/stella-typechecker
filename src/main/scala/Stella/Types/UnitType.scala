package Stella.Types

object UnitType extends Type {
  override def toString: String = "Unit"

  override def isSubtypeOf(other: Type): Boolean =
    other match
      case TopType => true
      case UnitType => true
      case _ => false
}
