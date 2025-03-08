package Stella

// TODO: all nulls should throw type errors

import Stella.Types._

class StellaVisitor extends StellaParserBaseVisitor[Any] {
  val functionsContext = new FunctionsContext()

  override def visitProgram(ctx: StellaParser.ProgramContext): Any = {
    ctx.decls.forEach {
      case funDecl: StellaParser.DeclFunContext => if visitDeclFun(funDecl) == null then println("Type error\n") else println("Program processed\n")
      case _ =>
        println("Ignored non-function declaration")
    }
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
    println(s"expr: ${expr.getText}")
    expr match {
      // Consts
      case _: StellaParser.ConstIntContext => if (TypeChecker.validate(NatType, expectedType)) NatType else null
      case _: StellaParser.ConstTrueContext => if (TypeChecker.validate(BoolType, expectedType)) BoolType else null
      case _: StellaParser.ConstFalseContext => if (TypeChecker.validate(BoolType, expectedType)) BoolType else null
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
        if visitExpr(ifCtx.condition, BoolType) == null then null else
          if TypeChecker.validate(visitExpr(ifCtx.thenExpr, expectedType),
            visitExpr(ifCtx.elseExpr, expectedType)) then expectedType else null
      // Application
      case appCtx: StellaParser.ApplicationContext =>

        //   Г |- t_1 : T_1 -> T_2   Г |- t_2 : T_1
        // ------------------------------------------ T-App
        //              Г |- t_1 t_2 : T_2

        val funcType = functionsContext.functionTypes(appCtx.fun.getText)
        val argType = TypeChecker.funcStack.top.varTypes(appCtx.args.get(0).getText)
        if TypeChecker.validate(funcType.argType, argType) && TypeChecker.validate(funcType.returnType, expectedType)
          then expectedType
          else null
      // Abstraction
      case absCtx: StellaParser.AbstractionContext =>

      //            Г, x : T_1 |- t : T_2
      // ------------------------------------------ T-Abs
      //        Г |- \x : T_1. t : T_1 -> T_2

        val arg = absCtx.paramDecls.get(0)
        val argType: Type = TypeChecker.ctxToType(arg.paramType)
        TypeChecker.funcStack.top.addVariable(Variable(arg.name.getText, argType))
        if visitExpr(absCtx.expr(), expectedType) == null then null else
          FunctionType(expectedType, argType)

      case _ =>
        println(s"Unsupported expression: ${expr.getText}")
        null
    }
  }
}