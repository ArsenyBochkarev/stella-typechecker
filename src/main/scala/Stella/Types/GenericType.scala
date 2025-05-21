package Stella.Types

class GenericType(typeName: String) extends Type {
  val name: String = typeName

  override def equals(other: Any): Boolean =
    other match
      case that: GenericType => that.name == name
      case _ => false
  override def toString: String = name
  override def hashCode(): Int = name.hashCode()
}
