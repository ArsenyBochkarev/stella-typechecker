package Stella.Types

object NatType extends Type {
  override def toString: String = "Nat"

  override def isSubtypeOf(other: Type): Boolean =
    other match
      case NatType => true
      case TopType => true
      case _ => false
}
