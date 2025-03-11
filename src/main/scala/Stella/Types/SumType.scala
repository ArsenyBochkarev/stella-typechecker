package Stella.Types

class SumType(tp: (Type, Type)) extends Type {
  var typePair: (Type, Type) = tp

  override def equals(obj: Any): Boolean = obj match {
    case that: SumType => typePair == that.typePair
    case _ => false
  }
  override def toString: String = s"${typePair._1} + ${typePair._2}"
  override def hashCode: Int = typePair.hashCode()
}
