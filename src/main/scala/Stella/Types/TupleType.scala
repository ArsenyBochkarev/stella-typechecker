package Stella.Types

import Stella.Error.ErrorManager
import Stella.Error.StellaError.ERROR_UNEXPECTED_TUPLE_LENGTH

class TupleType(types: List[Type]) extends Type {
  val elementsTypes: List[Type] = types

  override def equals(other: Any): Boolean =
    other match
      case that: TupleType =>
        if elementsTypes.size != that.elementsTypes.size then
          ErrorManager.registerError(ERROR_UNEXPECTED_TUPLE_LENGTH(
            toString, that.elementsTypes.size, elementsTypes.size))
          false
        else
          elementsTypes == that.elementsTypes
      case _ => false
  override def toString: String = s"{${elementsTypes.mkString(", ")}}"
  override def hashCode: Int = types.hashCode()

  override def replace(left: Type, right: Type): Type =
    TupleType(elementsTypes.map(elem => { elem.replace(left, right) }))
}
