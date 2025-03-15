package Stella.Types

class FunctionType(argumentType: Type, retType: Type) extends Type {
  val returnType: Type = retType
  val argType: Type = argumentType

  override def equals(other: Any): Boolean =
    other match 
      case that: FunctionType => argType == that.argType && returnType == that.returnType
      case _ => false
  override def toString: String = s"${argType} -> ${returnType}"
  override def hashCode(): Int = (returnType, argType).hashCode()
}
