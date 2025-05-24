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

  def substituteTypes(subst: Map[Type, Type]): FunctionType = {
    var from = argType
    var to = returnType

    for ((generic, actual) <- subst)
      from = substitute(from, generic, actual)
      to = substitute(to, generic, actual)

    FunctionType(from, to)
  }

  // This substitutes [genericType |-> actualType] in t by constructing new type
  def substitute(t: Type, genericType: Type, actualType: Type): Type = {
    t match {
      case fnTy: FunctionType =>
        FunctionType(
          substitute(fnTy.argType, genericType, actualType),
          substitute(fnTy.returnType, genericType, actualType)
        )
      case lstTy: ListType =>
        ListType(substitute(lstTy.listType, genericType, actualType))
      case tupTy: TupleType =>
        TupleType(tupTy.elementsTypes.map( ty => { substitute(tupTy, genericType, actualType) }))
      case sumTy: SumType =>
        SumType(
          substitute(sumTy.typePair._1, genericType, actualType),
          substitute(sumTy.typePair._2, genericType, actualType)
        )
      case varTy: VariantType =>
        VariantType(varTy.labelsMap.map(p => { (p._1, substitute(p._2, genericType, actualType) ) }))
      case univTy: UniversalType =>
        // Substitution for universal type disposes of genericType
        UniversalType(
          substitute(univTy.innerType, genericType, actualType), 
          univTy.outerTypes.filter(t => { t != genericType })
        )

      case genTy: GenericType =>
        if genTy == genericType then actualType else genTy
      case _ => t
    }
  }

  override def replace(left: Type, right: Type): Type =
    FunctionType(argType.replace(left, right), returnType.replace(left, right))
}
