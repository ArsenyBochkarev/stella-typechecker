package Stella.Types

class FunctionType(retType: Type, argumentType: Type) extends Type {
  val returnType: Type = retType
  val argType: Type = argumentType
  override def toString: String = s"${argType} -> ${returnType}"
}
