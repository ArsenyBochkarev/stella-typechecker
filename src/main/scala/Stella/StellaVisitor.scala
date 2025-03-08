package Stella

// TODO: all nulls should throw type errors

import Stella.Types.{FunctionType, *}
import Stella.Error.StellaError.*
import Stella.Error.*

class StellaVisitor extends StellaParserBaseVisitor[Any] {
  val functionsContext = new FunctionsContext()

  override def visitProgram(ctx: StellaParser.ProgramContext): Any = {
    ctx.decls.forEach {
      case funDecl: StellaParser.DeclFunContext => if visitDeclFun(funDecl) == null then println("Type error\n") else println(s"Function ${funDecl.name.getText} processed\n")
      case _ =>
        println("Ignored non-function declaration")
    }
    ErrorManager.outputErrors()
  }

  override def visitDeclFun(ctx: StellaParser.DeclFunContext): Type = {
    val typeContext: VarContext = if TypeChecker.funcStack.nonEmpty then TypeChecker.funcStack.top else VarContext()
    val arg = ctx.paramDecls.get(0)
    val argType: Type = TypeChecker.ctxToType(arg.paramType) // FIXME: exactly one argument for now
    typeContext.addVariable(Variable(varStr = arg.name.getText, argType))

    val expectedReturnType: Type = TypeChecker.ctxToType(ctx.returnType)
    TypeChecker.funcStack.push(typeContext)

    functionsContext.addFunction(ctx.name.getText, FunctionType(retType = expectedReturnType, argumentType = argType))
    val res = visitExpr(ctx.returnExpr, expectedReturnType)
    TypeChecker.funcStack.pop()
    res
  }

  private def visitExpr(expr: StellaParser.ExprContext, expectedType: Type): Type = {
    expr match {
      // Consts
      case constIntCtx: StellaParser.ConstIntContext => if (TypeChecker.validate(NatType, expectedType)) NatType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = constIntCtx.n.getText,
          expectedType.toString(), NatType.toString()))
        null
      case constTrueCtx: StellaParser.ConstTrueContext => if (TypeChecker.validate(BoolType, expectedType)) BoolType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = "True",
          expectedType.toString(), BoolType.toString()))
        null
      case _: StellaParser.ConstFalseContext => if (TypeChecker.validate(BoolType, expectedType)) BoolType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = "False",
          expectedType.toString(), BoolType.toString()))
        null
      // Succ, Pred
      case succCtx: StellaParser.SuccContext => // expr == Nat, inner expr == Nat
        if visitExpr(succCtx.expr(), NatType) == null then null else NatType
      case predCtx: StellaParser.PredContext => // expr == Nat, inner expr == Nat
        if visitExpr(predCtx.expr(), NatType) == null then null else NatType
      // IsZero
      case isZeroCtx: StellaParser.IsZeroContext => // expr == Bool, inner expr == Nat
        if visitExpr(isZeroCtx.expr(), NatType) == null then null else BoolType
      // Var
      case varCtx: StellaParser.VarContext => // expr == expectedType
        if TypeChecker.validate(TypeChecker.funcStack.top.varTypes(varCtx.name.getText), expectedType) then expectedType else null
      // If
      case ifCtx: StellaParser.IfContext => // expr == type(expr), cond == Bool, type(then) == type(else) == expectedType
        val condType = visitExpr(ifCtx.condition, BoolType)
        if condType == null then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
            ifCtx.condition.getText, condType.toString(), BoolType.toString()))
          null
        else
          val thenType = visitExpr(ifCtx.thenExpr, expectedType)
          val elseType = visitExpr(ifCtx.elseExpr, expectedType)
          if TypeChecker.validate(thenType, elseType) then expectedType
          else
            ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
              ifCtx.thenExpr.getText, thenType.toString(), elseType.toString()))
            null
      // Application
      case appCtx: StellaParser.ApplicationContext =>

        //   Г |- t_1 : T_1 -> T_2   Г |- t_2 : T_1
        // ------------------------------------------ T-App
        //              Г |- t_1 t_2 : T_2

        val funcType: FunctionType = functionsContext.functionTypes.get(appCtx.fun.getText) match {
          case Some(foundType) => foundType
          case _ =>
            visitExpr(appCtx.fun, null) match {
              case fTy: FunctionType => fTy
              case null =>
                null
            }
        }
        if funcType == null then
          ErrorManager.registerError(ERROR_NOT_A_FUNCTION(appCtx.fun.getText))
          return null
        val argType = TypeChecker.funcStack.top.varTypes(appCtx.args.get(0).getText)
        if !TypeChecker.validate(funcType.argType, argType) then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = appCtx.args.get(0).getText,
            funcType.argType.toString(), argType.toString()))
          return null
        if !TypeChecker.validate(funcType.returnType, expectedType) then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = appCtx.args.get(0).getText,
            funcType.returnType.toString(), expectedType.toString()))
          return null
        else expectedType
      // Abstraction
      case absCtx: StellaParser.AbstractionContext =>

      //            Г, x : T_1 |- t : T_2
      // ------------------------------------------ T-Abs
      //        Г |- \x : T_1. t : T_1 -> T_2

        expectedType match {
          case _: FunctionType =>
          case null =>
          case _ =>
            val actualType = visitExpr(absCtx.expr(), null)
            ErrorManager.registerError(
              ERROR_UNEXPECTED_LAMBDA(absCtx.expr().getText, actualType.toString(), expectedType.toString()))
            return null
        }

        val arg = absCtx.paramDecls.get(0)
        val argType: Type = TypeChecker.ctxToType(arg.paramType)
        TypeChecker.funcStack.top.addVariable(Variable(arg.name.getText, argType))
        if visitExpr(absCtx.expr(), expectedType) == null then null else FunctionType(argType, expectedType)

      case _ =>
        null
    }
  }
}