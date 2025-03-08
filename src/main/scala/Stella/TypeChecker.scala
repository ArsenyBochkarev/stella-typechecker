package Stella

import scala.collection.mutable.Stack
import Stella.Types._

import scala.collection.mutable

object TypeChecker {
  var funcStack: Stack[VarContext] = new mutable.Stack[VarContext]()

  def validate(actualType: Type, expectedType: Type): Boolean = actualType.equals(expectedType)

  def ctxToType(ctx: StellaParser.StellatypeContext): Type =
    ctx match {
      case _: StellaParser.TypeNatContext => NatType
      case _: StellaParser.TypeBoolContext => BoolType
      case funcTypeCtx: StellaParser.TypeFunContext =>
        FunctionType(ctxToType(funcTypeCtx.paramTypes.get(0)), ctxToType(funcTypeCtx.returnType))
      case _ => null // In fact, this one should be unsupported
    }
}
