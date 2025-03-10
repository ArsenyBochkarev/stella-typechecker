package Stella

import scala.collection.mutable.Stack
import Stella.Types._
import scala.jdk.CollectionConverters._

import scala.collection.mutable

object TypeChecker {
  var funcStack: Stack[VarContext] = new mutable.Stack[VarContext]()

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
      case _ =>
        println("Unexpected type!"); null // In fact, this one should be unsupported
    }
}
