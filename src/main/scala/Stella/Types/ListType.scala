package Stella.Types

class ListType(ty: Type) extends Type {
  val listType: Type = ty

  override def toString: String = s"List[${listType.toString}]"
  override def equals(other: Any): Boolean = other match {
    case that: ListType => listType == that.listType
    case _ => false
  }
}
