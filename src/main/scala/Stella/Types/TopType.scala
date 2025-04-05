package Stella.Types

object TopType extends Type {
  override def toString: String = "Top"

  override def isSubtypeOf(other: Type): Boolean =
    other match
      case TopType => true
      case _ => false
}
