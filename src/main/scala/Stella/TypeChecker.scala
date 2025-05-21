package Stella

import Stella.Error.ErrorManager
import Stella.Error.StellaError.{ERROR_DUPLICATE_RECORD_TYPE_FIELDS, ERROR_DUPLICATE_VARIANT_TYPE_FIELDS, ERROR_NOT_A_FUNCTION}

import scala.collection.mutable.Stack
import Stella.Types.*

import scala.jdk.CollectionConverters.*
import scala.collection.mutable

object TypeChecker {
  var funcStack: mutable.Stack[VarContext] = new mutable.Stack[VarContext]()

  def validate(actualType: Type, expectedType: Type): Boolean =
    if expectedType == null then true else actualType.equals(expectedType)

  def ctxToType(ctx: StellaParser.StellatypeContext): Type =
    ctx match {
      case _: StellaParser.TypeNatContext => NatType
      case _: StellaParser.TypeBoolContext => BoolType
      case _: StellaParser.TypeUnitContext => UnitType
      case funcTypeCtx: StellaParser.TypeFunContext =>
        FunctionType(ctxToType(funcTypeCtx.paramTypes.get(0)), ctxToType(funcTypeCtx.returnType))
      case tupleTypeCtx: StellaParser.TypeTupleContext =>
        TupleType(tupleTypeCtx.types.asScala.toList.map(innerType => ctxToType(innerType)))
      case recordTypeCtx: StellaParser.TypeRecordContext =>
        val recordLabels = recordTypeCtx.fieldTypes.asScala.toList.map(field => { field.label.getText })
        val recordTypes = recordTypeCtx.fieldTypes.asScala.toList.map(field => { ctxToType(field.type_) })
        val res = RecordType(recordLabels zip recordTypes)
        if recordLabels.toSet.size != recordLabels.size then
          val duplicates = recordLabels.diff(recordLabels.toSet.toList)
          for (dup <- duplicates)
            ErrorManager.registerError(ERROR_DUPLICATE_RECORD_TYPE_FIELDS(dup, res.toString))
          null
        else
          res
      case sumTypeCtx: StellaParser.TypeSumContext =>
        SumType((ctxToType(sumTypeCtx.left), ctxToType(sumTypeCtx.right)))
      case listTypeCtx: StellaParser.TypeListContext =>
        ListType(ctxToType(listTypeCtx.type_))
      case variantTypeCtx: StellaParser.TypeVariantContext =>
        val variantLabels = variantTypeCtx.fieldTypes.asScala.toList.map(field => { field.label.getText })
        val variantTypes = variantTypeCtx.fieldTypes.asScala.toList.map(field => { ctxToType(field.type_) })
        val res = VariantType(variantLabels zip variantTypes)
        if variantLabels.toSet.size != variantLabels.size then
          val duplicates = variantLabels.diff(variantLabels.toSet.toList)
          for (dup <- duplicates)
            ErrorManager.registerError(ERROR_DUPLICATE_VARIANT_TYPE_FIELDS(dup, res.toString))
          null
        else
          res
      case typeVarCtx: StellaParser.TypeVarContext => GenericType(typeVarCtx.getText)
      case typeForallCtx: StellaParser.TypeForAllContext =>
        UniversalType(
          ctxToType(typeForallCtx.type_),
          typeForallCtx.types.asScala.toList.map( generic => { GenericType(generic.getText) } )
        )
      case _ =>
        println("Unexpected type!"); null // In fact, this one should be unsupported
    }

  def checkUnresolvedType(t: Type, funcStack: Stack[VarContext]): Type = {
    t match {
      case fnTy: FunctionType =>
        if checkUnresolvedType(fnTy.argType, funcStack) != null then fnTy
        else
          checkUnresolvedType(fnTy.returnType, funcStack)
      case lstTy: ListType =>
        checkUnresolvedType(lstTy.listType, funcStack)
      case sumTy: SumType =>
        if checkUnresolvedType(sumTy.typePair._1, funcStack) != null then sumTy
        else
          checkUnresolvedType(sumTy.typePair._2, funcStack)
      case tupTy: TupleType =>
        for (elem <- tupTy.elementsTypes)
          if checkUnresolvedType(elem, funcStack) != null then return tupTy
        null

      case genericTy: GenericType =>
        if resolveGenericType(genericTy, funcStack) != null then genericTy
        else
          null
      case _ =>
        null
    }
  }
  def resolveGenericType(genericType: GenericType, funcStack: Stack[VarContext]): Type = {
    funcStack.top.typeVars.get(genericType.name) match {
      case Some(v) => v // Found type in current funcStack
      case null =>
        if funcStack.size > 1 then
          val funcStackCopy = funcStack
          funcStackCopy.pop()
          resolveGenericType(genericType, funcStackCopy)
        else
          null
    }
  }
}
