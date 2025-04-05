package Stella.Types

class SumType(tp: (Type, Type)) extends Type {
  var typePair: (Type, Type) = tp

  override def equals(obj: Any): Boolean = obj match {
    case that: SumType => typePair == that.typePair
    case _ => false
  }
  override def toString: String = s"${typePair._1} + ${typePair._2}"
  override def hashCode: Int = typePair.hashCode()

  override def isSubtypeOf(other: Type): Boolean =
    // No exact subtyping rule for sum types, so...
    // Based on a reference implementation:
    //    S_1 <: T_1   S_2 <: T_2
    // ----------------------------- S-SumType
    //    S_1 + S_2 <: T_1 + T_2

    other match
      case otherSum: SumType =>
        typePair._1.isSubtypeOf(otherSum.typePair._1) && typePair._2.isSubtypeOf(otherSum.typePair._2)
      case TopType => true
      case _ => false
}
