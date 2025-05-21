package Stella.Types

class UniversalType(t: Type, typeParams: List[Type]) extends Type {
  val innerType: Type = t
  val outerTypes: List[Type] = typeParams

  override def equals(other: Any): Boolean =
    other match
      case that: UniversalType => innerType == that.innerType && outerTypes == that.outerTypes
      case _ => false
  override def toString: String = s"forall ${outerTypes.mkString(", ")}. $innerType"
  override def hashCode(): Int = (innerType, outerTypes).hashCode()
}