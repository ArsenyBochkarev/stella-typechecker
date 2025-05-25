package Stella.Types

class ReferenceType(ty: Type) extends Type {
  val innerType: Type = ty

  override def toString: String = s"&${innerType.toString}"
  override def equals(other: Any): Boolean = other match {
    case that: ReferenceType => innerType == that.innerType
    case _ => false
  }

  override def replace(left: Type, right: Type): Type =
    ReferenceType(innerType.replace(left, right))
}