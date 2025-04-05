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

  override def isSubtypeOf(other: Type): Boolean =
    // No exact subtyping rule for tuples, so...
    //        S_1 <: T_1 ... S_n <: T_n
    // -------------------------------------- S-Tuple
    //   {S_1, ..., S_n} <: {T_1, ..., T_n}

    other match
      case otherTuple: TupleType =>
        elementsTypes.size == otherTuple.elementsTypes.size &&
          elementsTypes.zip(otherTuple.elementsTypes).forall((thisTy, otherTy) => { thisTy.isSubtypeOf(otherTy) })
      case TopType => true
      case _ => false
}
