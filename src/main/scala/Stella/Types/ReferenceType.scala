package Stella.Types

class ReferenceType(ty: Type) extends Type {
  val innerType: Type = ty

  override def toString: String = s"&${innerType.toString}"
  override def equals(other: Any): Boolean = other match {
    case that: ReferenceType => innerType == that.innerType
    case _ => false
  }
}