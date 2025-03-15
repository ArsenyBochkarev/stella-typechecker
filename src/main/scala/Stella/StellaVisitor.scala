package Stella

import Stella.Types.{FunctionType, *}
import Stella.Error.StellaError.*
import Stella.Error.*
import scala.jdk.CollectionConverters._
import scala.util.boundary, boundary.break

class StellaVisitor extends StellaParserBaseVisitor[Any] {
  private val functionsContext = new FunctionsContext()

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
            TypeChecker.funcStack.top.varTypes.get(appCtx.fun.getText) match {
              case Some(foundType: FunctionType) => foundType
              case Some(otherType) => // Some other type, error
                ErrorManager.registerError(ERROR_NOT_A_FUNCTION(appCtx.fun.getText, expectedType.toString()))
                return null
              case _ =>
                visitExpr(appCtx.fun, null) match {
                  case fTy: FunctionType => fTy
                  case _ =>
                    ErrorManager.registerError(ERROR_NOT_A_FUNCTION(appCtx.fun.getText, expectedType.toString()))
                    return null
                }
            }
        }

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
      // Parentheses (required to do application correctly)
      case pCtx: StellaParser.ParenthesisedExprContext =>
        visitExpr(pCtx.expr(), expectedType)
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
        expectedType match {
          case funcType: FunctionType =>
            if visitExpr(absCtx.returnExpr, funcType.returnType) == null then null
            else
              FunctionType(argType, funcType.returnType)
          case null =>
            val retType = visitExpr(absCtx.returnExpr, null)
            if retType == null then null
            else
              FunctionType(argType, retType)
        }
      // Sequence
      case seqCtx: StellaParser.SequenceContext =>

      //   Г |- t_1 : Unit   Г |- t_2 : T
      // ------------------------------------------ T-Seq
      //              Г |- t_1; t_2 : T

        val expr1Type = visitExpr(seqCtx.expr1, UnitType)
        expr1Type match {
          case UnitType => visitExpr(seqCtx.expr2, expectedType)
          case null => null
          case _ =>
            ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(seqCtx.expr1.getText, expr1Type.toString(), UnitType.toString()))
            null
        }

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
      // Sum types
      case inlCtx: StellaParser.InlContext =>

      //        Г |- t_1 : T_1
      // ------------------------------ T-Inl
      //    Г |- inl t_1 as T_1 + T_2 : T_1 + T_2

        expectedType match {
          case sumType: SumType =>
            if visitExpr(inlCtx.expr_, sumType.typePair._1) == null then null
            else
              sumType
          case null =>
              ErrorManager.registerError(ERROR_AMBIGUOUS_SUM_TYPE(inlCtx.expr().getText))
              null
          case _ =>
            val actualType = visitExpr(inlCtx.expr(), expectedType)
            ErrorManager.registerError(
              ERROR_UNEXPECTED_INJECTION(inlCtx.expr().getText, actualType.toString))
            null
        }

      //        Г |- t_2 : T_2
      // ------------------------------ T-inr
      //    Г |- inr t_2 as T_1 + T_2 : T_1 + T_2

      case inrCtx: StellaParser.InrContext =>
        expectedType match {
          case sumType: SumType =>
            if visitExpr(inrCtx.expr_, sumType.typePair._2) == null then null
            else
              sumType
          case null =>
            ErrorManager.registerError(ERROR_AMBIGUOUS_SUM_TYPE(inrCtx.expr().getText))
            null
          case _ =>
            val actualType = visitExpr(inrCtx.expr(), expectedType)
            ErrorManager.registerError(
              ERROR_UNEXPECTED_INJECTION(inrCtx.expr().getText, actualType.toString))
            null
        }
      case caseCtx: StellaParser.MatchContext =>

      //    Г |- t_1 : T_1 + T_2    Г, x : T_1 |- t_2 : C    Г, x : T_2 |- t_3 : C
      // ---------------------------------------------------------------------------- T-Case
      //                 Г |- case t_1 of inl x => t_2 | inr x => t_3 : C

        val exprType = visitExpr(caseCtx.expr_, null)
        exprType match {
          case sumType: SumType =>
            if caseCtx.cases.asScala.toList.isEmpty then
              ErrorManager.registerError(ERROR_ILLEGAL_EMPTY_MATCHING(caseCtx.expr().getText))
              null
            else
              var gotInl = false
              var gotInr = false
              var shouldNull = false
              var patternTypes = List.empty[Type]
              // Check all pattern types are same as expected type
              for (it <- caseCtx.cases.asScala.toList)
                val patternType = it.pattern_ match {
                  case pat: StellaParser.PatternInlContext =>
                    // If pattern is var, get its name and push to typeContext
                    gotInl = true
                    val name = pat.pattern_ match {
                      case varPattern: StellaParser.PatternVarContext =>
                        varPattern.name.getText
                      case _ => null
                    }
                    if name == null then visitExpr(it.expr_, expectedType)
                    else
                      val tc = TypeChecker.funcStack.top
                      tc.addVariable(Variable(name, sumType.typePair._1))
                      TypeChecker.funcStack.push(tc)
                      val res = visitExpr(it.expr_, expectedType)
                      TypeChecker.funcStack.pop
                      res
                  case pat: StellaParser.PatternInrContext =>
                    // If pattern is var, get its name and push to typeContext
                    gotInr = true
                    val name = pat.pattern_ match {
                      case varPattern: StellaParser.PatternVarContext =>
                        varPattern.name.getText
                      case _ => null
                    }
                    if name == null then visitExpr(it.expr_, expectedType)
                    else
                      val tc = TypeChecker.funcStack.top
                      tc.addVariable(Variable(name, sumType.typePair._2))
                      TypeChecker.funcStack.push(tc)
                      val res = visitExpr(it.expr_, expectedType)
                      TypeChecker.funcStack.pop
                      res
                  case _ => null
                }
                if patternType == null then shouldNull = true
                else
                  patternTypes = patternTypes :+ patternType
              if !(gotInr && gotInl) then
                ErrorManager.registerError(ERROR_NONEXHAUSTIVE_MATCH_PATTERNS(caseCtx.expr().getText))
                null
              else
                if shouldNull then null
                else
                  patternTypes.find(p =>
                    !TypeChecker.validate(p, expectedType)) match {
                    case Some(p) =>
                      ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText, p.toString, expectedType.toString))
                      null
                    case _ =>
                      patternTypes.head
                  }
          case _ => null
        }
      // Lists
      case consCtx: StellaParser.ConsListContext =>

      //   Г |- t_1 : T   Г |- t_2 : List[T]
      // ----------------------------------------- T-Cons
      //    Г |- cons[T] t_1 t_2 : List[T]

        expectedType match {
          case expectedListType: ListType =>
            val headType = visitExpr(consCtx.head, null)
            if headType == null then null else
              if expectedType != null && !TypeChecker.validate(headType, expectedListType.listType) then
                ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
                  consCtx.head.toString(), headType.toString, expectedType.toString))
                null
              else
                val resultType = ListType(headType)
                val tailType = visitExpr(consCtx.tail, resultType)
                if tailType == null then null
                else
                  resultType
          case null =>
            val headType = visitExpr(consCtx.head, null)

            val resultType = ListType(headType)
            val tailType = visitExpr(consCtx.tail, resultType)

            if headType == null then
              if tailType == null then
                ErrorManager.registerError(ERROR_AMBIGUOUS_LIST(expr.getText))
              null
            else
              if tailType == null then null
              else
                resultType
          case _ =>
            val actualType = visitExpr(consCtx, null)
            ErrorManager.registerError(ERROR_UNEXPECTED_LIST(expectedType.toString, actualType.toString))
            null
        }
      case headCtx: StellaParser.HeadContext =>

      //     Г |- t : List[T]
      // ----------------------- T-Head
      //    Г |- head[T] t : T

        val listType = visitExpr(headCtx.list, null)
        listType match {
          case expectedListType: ListType =>
            if TypeChecker.validate(expectedListType.listType, expectedType) then expectedListType
            else
              ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
                headCtx.list.getText, expectedListType.toString, ListType(expectedType).toString
              ))
              null
          case _ =>
            ErrorManager.registerError(ERROR_NOT_A_LIST(
              headCtx.list.getText, listType.toString, expectedType.toString))
            null
        }
      case tailCtx: StellaParser.TailContext =>

      //        Г |- t : List[T]
      // ----------------------------- T-Tail
      //    Г |- tail[T] t : List[T]

        val listType = visitExpr(tailCtx.list, null)
        listType match {
          case tailListType: ListType =>
            if TypeChecker.validate(tailListType, expectedType) then tailListType
            else
              ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
                tailCtx.list.getText, tailListType.toString, expectedType.toString))
              null
          case null =>
            null
          case _ =>
            ErrorManager.registerError(ERROR_NOT_A_LIST(
              tailCtx.list.getText, listType.toString, expectedType.toString))
            null
        }
      case isEmptyCtx: StellaParser.IsEmptyContext =>

      //        Г |- t : List[T]
      // ----------------------------- T-IsNil
      //    Г |- isNil[T] t : Bool

        isEmptyCtx.list
        val exprType = visitExpr(isEmptyCtx.list, null)
        exprType match {
          case expectedListType: ListType =>
            BoolType
          case null =>
            null
          case _ =>
            ErrorManager.registerError(ERROR_NOT_A_LIST(
            isEmptyCtx.list.getText, exprType.toString, expectedType.toString))
            null
        }
      case listCtx: StellaParser.ListContext =>

      // No rule for []-constructed list, so...
      // Сheck all the expressions for expectedType.listType, smth like that:
      //     Г |- t_1 : T  ...  Г |- t_n : T
      //  -------------------------------------
      //      Г |- [t_1, ..., t_n] : List[T]

        val exprScalaList = listCtx.exprs.asScala.toList
        expectedType match {
          case expectedListType: ListType =>
            val exprTypes = exprScalaList.map( innerExpr =>
              {visitExpr(innerExpr, expectedListType.listType)} )
            if exprTypes.isEmpty || exprTypes.contains(null) then null
            else
              ListType(exprTypes.head)
          case null =>
            if exprScalaList.isEmpty then
              ErrorManager.registerError(ERROR_AMBIGUOUS_LIST(listCtx.expr.getText))
              null
            else
              val exprTypes = exprScalaList.map( innerExpr =>
                {visitExpr(innerExpr, null)} )
              if exprTypes.contains(null) then null
              else
                ListType(exprTypes.head)
          case _ =>
            val actualType = visitExpr(listCtx, null)
            ErrorManager.registerError(ERROR_UNEXPECTED_LIST(expectedType.toString, actualType.toString))
            null
        }
      // Fixpoint combinator
      case fixCtx: StellaParser.FixContext =>

      //   Г |- t_1 : T_1 -> T_1
      // ------------------------- T-Fix
      //    Г |- fix t_1 : T_1

        // First, try to find it in context
        var exprType = functionsContext.functionTypes.get(fixCtx.expr_.getText) match {
          case Some(foundType: FunctionType) => foundType
          case _ =>
            // It's not a top-level function
            // It also can be a variable
            TypeChecker.funcStack.top.varTypes.get(fixCtx.expr_.getText) match {
              case Some(foundType: FunctionType) => foundType // Proceed
              case Some(otherType) => // Some other type, error
                ErrorManager.registerError(ERROR_NOT_A_FUNCTION(
                  fixCtx.expr_.getText, FunctionType(expectedType, expectedType).toString))
                return null
              case _ => null // Nothing, proceed
            }
        }
        val expectedFunctionType = if expectedType == null then null else FunctionType(expectedType, expectedType)
        if exprType == null then // Found nothing, infer
          exprType = visitExpr(fixCtx.expr_, expectedFunctionType) match {
            case functionRes: FunctionType => functionRes
            case null => null
            case _ =>
              ErrorManager.registerError(ERROR_NOT_A_FUNCTION(fixCtx.expr_.getText, expectedType.toString()))
              null
          }
        if exprType == null then null
        else
            if TypeChecker.validate(exprType, expectedFunctionType) then
              exprType.returnType
            else
              ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
                fixCtx.expr.getText, exprType.toString, expectedFunctionType.toString))
              null
      case _ =>
        null
    }
  }
}