package Stella.Types

class TupleType(types: List[Type]) extends Type {
  val elementsTypes: List[Type] = types

  override def equals(other: Any): Boolean =
    other match
      case that: TupleType => elementsTypes == that.elementsTypes
      case _ => false
  override def toString: String = s"{${elementsTypes.mkString(", ")}}"
  override def hashCode: Int = types.hashCode()
}
