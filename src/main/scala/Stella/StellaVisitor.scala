package Stella

import Stella.Types.{FunctionType, *}
import Stella.Error.StellaError.*
import Stella.Error.*
import scala.jdk.CollectionConverters._
import scala.util.boundary, boundary.break

class StellaVisitor extends StellaParserBaseVisitor[Any] {
  val functionsContext = new FunctionsContext()

  override def visitProgram(ctx: StellaParser.ProgramContext): Any = {
    boundary {
      ctx.decls.forEach {
        case funDecl: StellaParser.DeclFunContext =>
          if visitDeclFun(funDecl) == null then
            println("Type error\n")
            break()
          else println(s"Function ${funDecl.name.getText} processed\n")
        case _ =>
          println("Ignored non-function declaration")
      }
    }
    ErrorManager.outputErrors()
  }

  override def visitDeclFun(ctx: StellaParser.DeclFunContext): Type = {
    val typeContext: VarContext = if TypeChecker.funcStack.nonEmpty then TypeChecker.funcStack.top else VarContext()
    val arg = ctx.paramDecls.get(0)
    val argType: Type = TypeChecker.ctxToType(arg.paramType) // TODO: #nullary-functions extension
    typeContext.addVariable(Variable(varStr = arg.name.getText, argType))

    val expectedReturnType: Type = TypeChecker.ctxToType(ctx.returnType)
    if expectedReturnType == null then return null
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
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(constIntCtx.n.getText,
          NatType.toString(), expectedType.toString()))
        null
      case constTrueCtx: StellaParser.ConstTrueContext => if (TypeChecker.validate(BoolType, expectedType)) BoolType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText,
          BoolType.toString(), expectedType.toString()))
        null
      case _: StellaParser.ConstFalseContext => if (TypeChecker.validate(BoolType, expectedType)) BoolType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText,
          BoolType.toString(), expectedType.toString()))
        null
      case exprCtx: StellaParser.ConstUnitContext => if (TypeChecker.validate(UnitType, expectedType)) UnitType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText,
          UnitType.toString(), expectedType.toString()))
        null
      // Succ, Pred
      case succCtx: StellaParser.SuccContext => // expr == Nat, inner expr == Nat
        visitExpr(succCtx.expr(), NatType)
      case predCtx: StellaParser.PredContext => // expr == Nat, inner expr == Nat
        visitExpr(predCtx.expr(), NatType)
      // IsZero
      case isZeroCtx: StellaParser.IsZeroContext => // expr == Bool, inner expr == Nat
        if visitExpr(isZeroCtx.expr(), NatType) == null then null else BoolType
      // Var
      case varCtx: StellaParser.VarContext => // expr == expectedType
        val varType: Type = TypeChecker.funcStack.top.varTypes.get(varCtx.name.getText) match {
          case Some(v) => v
          case _ =>
            ErrorManager.registerError(ERROR_UNDEFINED_VARIABLE(varCtx.name.getText))
            null
        }
        if varType == null then null
        else
          if TypeChecker.validate(varType, expectedType) then varType else null
      // If
      case ifCtx: StellaParser.IfContext => // expr == type(expr), cond == Bool, type(then) == type(else) == expectedType
        val condType = visitExpr(ifCtx.condition, BoolType)
        if condType == null then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
            ifCtx.condition.getText, condType.toString(), BoolType.toString()))
          return null
        val thenType = visitExpr(ifCtx.thenExpr, expectedType)
        val elseType = visitExpr(ifCtx.elseExpr, expectedType)
        if TypeChecker.validate(thenType, elseType) then thenType
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
              case _ => null
            }
        }
        if funcType == null then
          ErrorManager.registerError(ERROR_NOT_A_FUNCTION(appCtx.fun.getText, expectedType.toString()))
          return null
        val argType = TypeChecker.funcStack.top.varTypes.get(appCtx.args.get(0).getText) match {
          case Some(t) => t
          case _ =>
            visitExpr(appCtx.args.get(0), null)
        }
        if !TypeChecker.validate(funcType.argType, argType) then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = appCtx.args.get(0).getText,
            funcType.argType.toString(), argType.toString()))
          return null
        if TypeChecker.validate(funcType.returnType, expectedType) then
          funcType.returnType
        else
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = appCtx.args.get(0).getText,
            funcType.returnType.toString(), expectedType.toString()))
          null

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
      // Sequence
      case seqCtx: StellaParser.SequenceContext =>

      //   Г |- t_1 : Unit   Г |- t_2 : T
      // ------------------------------------------ T-Seq
      //              Г |- t_1; t_2 : T

        val expr1Type = visitExpr(seqCtx.expr1, UnitType)
        if expr1Type == null then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(seqCtx.expr1.getText, expr1Type.toString(), UnitType.toString()))
          return null
        visitExpr(seqCtx.expr2, expectedType)
      // Type ascription
      case ascCtx: StellaParser.TypeAscContext =>

        //      Г |- t : T
        // --------------------- T-Ascribe
        //    Г |- t as T : T

        val innerType = visitExpr(ascCtx.expr(), expectedType)
        if innerType == null then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(ascCtx.expr().getText, innerType.toString(), expectedType.toString()))
          null else
            val ctxType = TypeChecker.ctxToType(ascCtx.type_)
            if TypeChecker.validate(ctxType, expectedType) then ctxType
            else
              ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(ascCtx.expr().getText, ctxType.toString(), expectedType.toString()))
              null
      // Let
      case letCtx: StellaParser.LetContext =>

      //   Г |- t_1 : T_1   Г, x : T_1 |- t_2 : T_2
      // ------------------------------------------ T-Let
      //        Г |- let x = t_1 in t_2 : T_2

        val rhsType = visitExpr(letCtx.patternBinding.rhs, null)
        if rhsType == null then null
        else
          TypeChecker.funcStack.top.addVariable(Variable(letCtx.patternBinding.pat.getText, rhsType))
          visitExpr(letCtx.expr(), expectedType)
      // Tuples and Pairs
      case tupleCtx: StellaParser.TupleContext =>

        //    Г |- t_1 : T_1  ...  Г |- t_n : T_n
        // ------------------------------------------ T-Tuple
        //   Г |- {t_1, ..., t_n} : {T_1, ..., T_n}

        // TODO: check for length in #pairs extension
        expectedType match {
          case tupleType: TupleType =>
            val resType = TupleType(
              (tupleCtx.exprs.asScala.toList zip tupleType.elementsTypes).map(
                (innerExpr, innerType) => visitExpr(innerExpr, innerType)))
            if resType.elementsTypes.contains(null) then null else resType
          case null =>
            TupleType(tupleCtx.exprs.asScala.toList.map(innerExpr => visitExpr(innerExpr, null)))
          case _ =>
            val actualType = visitExpr(tupleCtx.expr, null)
            ErrorManager.registerError(ERROR_UNEXPECTED_TUPLE(
              tupleCtx.expr.getText, actualType.toString(), expectedType.toString()))
            null
        }
      case dotTupleCtx: StellaParser.DotTupleContext =>

      //    Г |- t : {T_1,  ...,  T_n}
      // -------------------------------- T-Proj
      //         Г |- t.j : T_j

        visitExpr(dotTupleCtx.expr_, null) match {
          case tupleType: TupleType =>
            val index = dotTupleCtx.index.getText.toInt - 1
            if index > tupleType.elementsTypes.size || index == 0 then
              ErrorManager.registerError(
                ERROR_TUPLE_INDEX_OUT_OF_BOUNDS(dotTupleCtx.expr().getText, tupleType.elementsTypes.size, index))
              null
            else
              val elemTy = tupleType.elementsTypes(index)
              if TypeChecker.validate(elemTy, expectedType) then elemTy
              else
                null
          case _ =>
            ErrorManager.registerError(ERROR_NOT_A_TUPLE(dotTupleCtx.expr().getText))
            null
        }
      // Records
      case recordCtx: StellaParser.RecordContext =>

      //              Г |- t_1 : T_1  ...  Г |- t_n : T_n
      // ------------------------------------------------------------------ T-Tuple
      //   Г |- {l_1 = t_1, ..., l_n = t_n} : {l_1 : T_1, ..., l_n : T_n}

        val labels = recordCtx.bindings.asScala.toList.map(bind => { bind.name.getText })
        val types = recordCtx.bindings.asScala.toList.map(bind => { visitExpr(bind.rhs, null) })
        val recTy = RecordType(labels zip types)

        if labels.toSet.size != labels.size then
          val duplicates = labels.diff(labels.toSet.toList)
          for (dup <- duplicates)
            ErrorManager.registerError(ERROR_DUPLICATE_RECORD_FIELDS(dup, recTy.toString))
          return null

        expectedType match {
          case recordType: RecordType =>
            if types.contains(null) then return null
            if TypeChecker.validate(recTy, expectedType) then
              recTy
            else null
          case null =>
            if types.contains(null) then return null
            recTy
          case _ =>
            ErrorManager.registerError(ERROR_UNEXPECTED_RECORD(expectedType.toString(), recTy.toString()))
            null
        }
      case dotRecordCtx: StellaParser.DotRecordContext =>

      //    Г |- t : {l_1 : T_1, ...,  l_n : T_n}
      // ------------------------------------------ T-Proj
      //    Г |- t.l_j : T_j

        val exprType = visitExpr(dotRecordCtx.expr(), null)
        exprType match {
          case recordType: RecordType =>
            val label = dotRecordCtx.label.getText
            // Check if it is from exprType
            recordType.labelsMap.find((l, t) => l == label) match {
              case Some(v) =>
                if TypeChecker.validate(v._2, expectedType) then v._2
                else
                  ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
                    label, v._2.toString, expectedType.toString))
                  null
              case _ =>
                ErrorManager.registerError(ERROR_UNEXPECTED_FIELD_ACCESS(label, recordType.toString))
                null
            }
          case null => null
          case _ =>
            ErrorManager.registerError(
              ERROR_NOT_A_RECORD(dotRecordCtx.expr().getText, exprType.toString, expectedType.toString))
            null
        }
      case _ =>
        null
    }
  }
}