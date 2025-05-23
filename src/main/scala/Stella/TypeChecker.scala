package Stella

import Stella.Error.ErrorManager
import Stella.Error.StellaError.{ERROR_DUPLICATE_RECORD_TYPE_FIELDS, ERROR_DUPLICATE_VARIANT_TYPE_FIELDS, ERROR_UNEXPECTED_SUBTYPE, ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION}

import scala.collection.mutable.Stack
import Stella.Types.*

import scala.jdk.CollectionConverters.*
import scala.collection.mutable

object TypeChecker {
  var funcStack: mutable.Stack[VarContext] = new mutable.Stack[VarContext]()
  var exceptionType: Type = null
  var isSubtypingEnabled = false
  var isAmbiguousTypeAsBottom = false

  def validate(ctx: String, actualType: Type, expectedType: Type): Boolean =
    if expectedType == null then true
    else
      if actualType.equals(expectedType) then true
      else
        if isSubtypingEnabled then
          if actualType.isSubtypeOf(expectedType) then true
          else
            ErrorManager.registerError(ERROR_UNEXPECTED_SUBTYPE(ctx, actualType.toString, expectedType.toString))
            false
        else
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(ctx, actualType.toString, expectedType.toString))
          false

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
      case refTypeCtx: StellaParser.TypeRefContext =>
        ReferenceType(ctxToType(refTypeCtx.type_))
      case botTypeCtx: StellaParser.TypeBottomContext =>
        BotType
      case topTypeCtx: StellaParser.TypeTopContext =>
        TopType
      case parensCtx: StellaParser.TypeParensContext => ctxToType(parensCtx.type_)
      case _ =>
        println("Unexpected type!"); null // In fact, this one should be unsupported
    }
}
