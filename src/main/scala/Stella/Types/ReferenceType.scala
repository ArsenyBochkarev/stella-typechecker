package Stella.Types

class ReferenceType(ty: Type) extends Type {
  val innerType: Type = ty

  override def toString: String = s"&${innerType.toString}"
  override def equals(other: Any): Boolean = other match {
    case that: ReferenceType => innerType == that.innerType
    case _ => false
  }

  override def isSubtypeOf(other: Type): Boolean =

    //    S <: T   T <: S
    // --------------------- S-Ref
    //    Ref[S] <: Ref[T]

    other match
      case otherRef: ReferenceType =>
        otherRef.innerType.isSubtypeOf(innerType) && innerType.isSubtypeOf(otherRef.innerType)
      case TopType => false
      case _ => false
}
