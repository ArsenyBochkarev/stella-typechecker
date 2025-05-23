package Stella

import Stella.Types.{FunctionType, *}
import Stella.Error.StellaError.*
import Stella.Error.*

import scala.jdk.CollectionConverters.*
import scala.util.boundary
import boundary.break

class StellaVisitor extends StellaParserBaseVisitor[Any] {
  private val functionsContext = new FunctionsContext()

  override def visitProgram(ctx: StellaParser.ProgramContext): Any = {
    // Search for 'main' first
    var mainFound = false
    boundary {
      ctx.decls.forEach {
        case funDecl: StellaParser.DeclFunContext =>
          if funDecl.name.getText == "main" then
            mainFound = true
            break()
        case _ =>
      }
    }
    if !mainFound then
      ErrorManager.registerError(ERROR_MISSING_MAIN())
    else
      boundary {
        inspectExtensions(ctx)
        ctx.decls.forEach {
          case funDecl: StellaParser.DeclFunContext =>
            if funDecl.name.getText == "main" then mainFound = true
            if visitDeclFun(funDecl) == null then
              break()
            else println(s"Function ${funDecl.name.getText} processed")
          case excDecl: StellaParser.DeclExceptionTypeContext =>
            visitDeclExceptionType(excDecl)
          case _ =>
            println("Ignored non-function/non-exception declaration")
        }
      }
    ErrorManager.outputErrors()
  }

  private def inspectExtensions(ctx: StellaParser.ProgramContext): Unit = {
    ctx.extensions.asScala.foreach {
      case extInstance: StellaParser.AnExtensionContext =>
        extInstance.extensionNames.forEach ( ext => ext.getText match
          case "#structural-subtyping" => TypeChecker.isSubtypingEnabled = true
          case "#ambiguous-type-as-bottom" => TypeChecker.isAmbiguousTypeAsBottom = true
          case _ =>
        )
      case _ =>
    }
  }

  override def visitDeclExceptionType(ctx: StellaParser.DeclExceptionTypeContext): Unit = {
    TypeChecker.exceptionType = TypeChecker.ctxToType(ctx.exceptionType)
  }

  override def visitDeclFun(ctx: StellaParser.DeclFunContext): Type = {
    val typeContext: VarContext = if TypeChecker.funcStack.nonEmpty then TypeChecker.funcStack.top else VarContext()
    val arg = ctx.paramDecls.get(0)
    val argType: Type = TypeChecker.ctxToType(arg.paramType)
    typeContext.addVariable(Variable(varStr = arg.name.getText, argType))

    val expectedReturnType: Type = TypeChecker.ctxToType(ctx.returnType)
    if expectedReturnType == null then return null
    TypeChecker.funcStack.push(typeContext)

    functionsContext.addFunction(ctx.name.getText, FunctionType(retType = expectedReturnType, argumentType = argType))
    val res = visitExpr(ctx.returnExpr, expectedReturnType)
    if res == null then null
    else
      if TypeChecker.validate(ctx.returnExpr.getText, res, expectedReturnType) then
        TypeChecker.funcStack.pop()
        res
      else
        TypeChecker.funcStack.pop()
        null
  }

  private def visitExpr(expr: StellaParser.ExprContext, expectedType: Type): Type = {
    expr match {
      // Consts
      case constIntCtx: StellaParser.ConstIntContext =>
        if TypeChecker.validate(constIntCtx.n.getText, NatType, expectedType) then NatType
        else
          null
      case constTrueCtx: StellaParser.ConstTrueContext =>
        if TypeChecker.validate(expr.getText, BoolType, expectedType) then BoolType
        else
          null
      case _: StellaParser.ConstFalseContext =>
        if TypeChecker.validate(expr.getText, BoolType, expectedType) then BoolType
        else
          null
      case exprCtx: StellaParser.ConstUnitContext =>
        if TypeChecker.validate(expr.getText, UnitType, expectedType) then UnitType
        else
          null

      // Succ, Pred
      case succCtx: StellaParser.SuccContext => // expr == Nat, inner expr == Nat
        val resType = visitExpr(succCtx.expr(), NatType)
        if resType == null then null
        else
          if !TypeChecker.validate(succCtx.expr().toString, resType, expectedType) then null
          else
            resType
      case predCtx: StellaParser.PredContext => // expr == Nat, inner expr == Nat
        val resType = visitExpr(predCtx.expr(), NatType)
        if resType == null then null
        else
          if !TypeChecker.validate(predCtx.expr().toString, resType, expectedType) then null
          else
            resType

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
          if TypeChecker.validate(varCtx.getText, varType, expectedType) then varType
          else
            null

      // If
      case ifCtx: StellaParser.IfContext => // expr == type(expr), cond == Bool, type(then) == type(else) == expectedType
        val condType = visitExpr(ifCtx.condition, BoolType)
        if condType == null then return null
        val thenType = visitExpr(ifCtx.thenExpr, expectedType)
        val elseType = visitExpr(ifCtx.elseExpr, expectedType)
        if thenType == null || elseType == null || !TypeChecker.validate(ifCtx.thenExpr.getText, thenType, elseType) then null
        else
          if !TypeChecker.validate(ifCtx.thenExpr.getText, thenType, expectedType) then null
          else
            thenType

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
            visitExpr(appCtx.args.get(0), funcType.argType)
        }
        if !TypeChecker.validate(appCtx.args.get(0).getText, funcType.argType, argType) then null
        else
          if TypeChecker.validate(appCtx.fun.getText, funcType.returnType, expectedType) then funcType.returnType
          else
            null

      // Parentheses (required to do application correctly)
      case pCtx: StellaParser.ParenthesisedExprContext =>
        visitExpr(pCtx.expr(), expectedType)

      // Abstraction
      case absCtx: StellaParser.AbstractionContext =>

      //            Г, x : T_1 |- t : T_2
      // ------------------------------------------ T-Abs
      //        Г |- \x : T_1. t : T_1 -> T_2

        val arg = absCtx.paramDecls.get(0)
        val argType: Type = TypeChecker.ctxToType(arg.paramType)
        val typeContext = TypeChecker.funcStack.top
        typeContext.addVariable(Variable(arg.name.getText, argType))
        TypeChecker.funcStack.push(typeContext)

        expectedType match {
          case funcType: FunctionType =>
            if visitExpr(absCtx.returnExpr, funcType.returnType) == null then
              TypeChecker.funcStack.pop(); null
            else
              val resType = FunctionType(argType, funcType.returnType)
              if !TypeChecker.validate(absCtx.getText, resType, expectedType) then
                TypeChecker.funcStack.pop(); null
              else
                TypeChecker.funcStack.pop(); resType
          case null =>
            val retType = visitExpr(absCtx.returnExpr, null)
            if retType == null then
              TypeChecker.funcStack.pop(); null
            else
              TypeChecker.funcStack.pop(); FunctionType(argType, retType)
          case _ =>
            val actualType = visitExpr(absCtx.expr(), null) match {
              case t: Any => t
              case null => TypeChecker.funcStack.pop(); return null
            }
            ErrorManager.registerError(
              ERROR_UNEXPECTED_LAMBDA(absCtx.expr().getText, actualType.toString(), expectedType.toString()))
            TypeChecker.funcStack.pop()
            null
        }

      // Sequence
      case seqCtx: StellaParser.SequenceContext =>

      //   Г |- t_1 : Unit   Г |- t_2 : T
      // ------------------------------------------ T-Seq
      //              Г |- t_1; t_2 : T

        val expr1Type = visitExpr(seqCtx.expr1, UnitType)
        expr1Type match {
          case UnitType =>
            val expr2Type = visitExpr(seqCtx.expr2, expectedType)
            if expr2Type == null then null
            else
              if TypeChecker.validate(seqCtx.expr2.getText, expr2Type, expectedType) then expr2Type
              else
                null
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

        val correctedExpectedType = TypeChecker.ctxToType(ascCtx.type_)
        val innerType = visitExpr(ascCtx.expr_, if expectedType == null then correctedExpectedType else expectedType)
        if innerType == null then null
        else
            val ctxType = TypeChecker.ctxToType(ascCtx.type_)
            if TypeChecker.validate(ascCtx.expr().getText, ctxType, expectedType) then ctxType
            else
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

        expectedType match {
          case tupleType: TupleType =>
            val resType = TupleType(
              (tupleCtx.exprs.asScala.toList zip tupleType.elementsTypes).map(
                (innerExpr, innerType) => visitExpr(innerExpr, innerType)))
            if resType.elementsTypes.contains(null) then null else resType
          case null =>
            TupleType(tupleCtx.exprs.asScala.toList.map(innerExpr => visitExpr(innerExpr, null)))
          case _ =>
            val actualType = visitExpr(tupleCtx.expr, null) match {
              case t: Any => t
              case null => return null
            }
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
              if TypeChecker.validate(dotTupleCtx.expr_.getText + "." + index, elemTy, expectedType) then elemTy
              else
                null
          case _ =>
            ErrorManager.registerError(ERROR_NOT_A_TUPLE(dotTupleCtx.expr().getText))
            null
        }

      // Records
      case recordCtx: StellaParser.RecordContext =>

      //              Г |- t_1 : T_1  ...  Г |- t_n : T_n
      // ------------------------------------------------------------------ T-Rcd
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
            if TypeChecker.validate(recordCtx.toString, recTy, expectedType) then recTy
            else
              null
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
            // Check if it is from recordType
            recordType.labelsMap.find((l, t) => l == label) match {
              case Some(v) =>
                if TypeChecker.validate(label, v._2, expectedType) then v._2
                else
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

      //                Г |- t_1 : T_1
      // ------------------------------------------- T-Inl
      //    Г |- inl t_1 as T_1 + T_2 : T_1 + T_2

        expectedType match {
          case sumType: SumType =>
            val leftTy = visitExpr(inlCtx.expr_, sumType.typePair._1)
            if leftTy == null then null
            else
              if !TypeChecker.validate(inlCtx.expr_.getText, leftTy, sumType.typePair._1) then null
              else
                sumType
          case null =>
            if TypeChecker.isAmbiguousTypeAsBottom then BotType
            else
              ErrorManager.registerError(ERROR_AMBIGUOUS_SUM_TYPE(inlCtx.expr().getText))
              null
          case _ =>
            val actualType = visitExpr(inlCtx.expr(), expectedType) match {
              case t: Any => t
              case null => return null
            }
            ErrorManager.registerError(
              ERROR_UNEXPECTED_INJECTION(inlCtx.expr().getText, actualType.toString))
            null
        }

      case inrCtx: StellaParser.InrContext =>

      //                Г |- t_2 : T_2
      // ------------------------------------------- T-Inr
      //    Г |- inr t_2 as T_1 + T_2 : T_1 + T_2

        expectedType match {
          case sumType: SumType =>
            val rightTy = visitExpr(inrCtx.expr_, sumType.typePair._2)
            if rightTy == null then null
            else
              if !TypeChecker.validate(inrCtx.expr_.getText, rightTy, sumType.typePair._2) then null
              else
                sumType
          case null =>
            if TypeChecker.isAmbiguousTypeAsBottom then BotType
            else
              ErrorManager.registerError(ERROR_AMBIGUOUS_SUM_TYPE(inrCtx.expr().getText))
              null
          case _ =>
            val actualType = visitExpr(inrCtx.expr(), expectedType) match {
              case t: Any => t
              case null => return null
            }
            ErrorManager.registerError(
              ERROR_UNEXPECTED_INJECTION(inrCtx.expr().getText, actualType.toString))
            null
        }

      case caseCtx: StellaParser.MatchContext => // This used in both sum and variant types
        if caseCtx.cases.asScala.toList.isEmpty then
          ErrorManager.registerError(ERROR_ILLEGAL_EMPTY_MATCHING(caseCtx.expr().getText))
          return null
        val exprType = visitExpr(caseCtx.expr_, null)
        exprType match {
          case sumType: SumType =>

          //    Г |- t_1 : T_1 + T_2    Г, x : T_1 |- t_2 : C    Г, x : T_2 |- t_3 : C
          // ---------------------------------------------------------------------------- T-Case
          //                 Г |- case t_1 of inl x => t_2 | inr x => t_3 : C

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
                case _ =>
                  ErrorManager.registerError(ERROR_UNEXPECTED_PATTERN_FOR_TYPE(
                    it.pattern_.getText, sumType.toString))
                  null
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
                patternTypes.find(p => !TypeChecker.validate(expr.getText, p, expectedType)) match {
                  case Some(p) =>
                    null
                  case _ =>
                    patternTypes.head
                }
          case expectedVariantType: VariantType =>

          //               Г |- t_0 : <l_1 : T_1, ..., l_n : T_n>
          //         Г, x_1 : T_1 |- t_1 : C ... Г, x_n : T_n |- t_n : C
          // ---------------------------------------------------------------------- T-CaseVariant
          //   Г |- case t_0 of <l_1 = x_1> => t_1 | ... | <l_n = x_n> => t_n : C

            var shouldNull = false
            var actualPatterns = List.empty[(String, Type)]
            // Check all pattern types are same as expected type

            // First, collect all the types from pattern list
            for (it <- caseCtx.cases.asScala.toList)
              val (patternLabel, patternType) = it.pattern_ match {
                case varPat: StellaParser.PatternVariantContext =>
                  val patName = varPat.pattern_ match {
                    case varPattern: StellaParser.PatternVarContext =>
                      varPattern.name.getText
                    case _ => null
                  }
                  if patName == null then (null, null)
                  else
                    val scope = TypeChecker.funcStack.top
                    // Get type of pattern from expectedVariantType
                    val expectedPatType = expectedVariantType.labelsMap.find(
                      (l, t) => l == varPat.label.getText) match {
                      case Some(v) => v._2
                      case _ =>
                        ErrorManager.registerError(ERROR_UNEXPECTED_VARIANT_LABEL(
                          varPat.label.getText, expectedVariantType.toString))
                        null
                    }
                    if expectedPatType == null then (null, null)
                    else
                      scope.addVariable(Variable(patName, expectedPatType))
                      TypeChecker.funcStack.push(scope)
                      val res = visitExpr(it.expr_, expectedType)
                      TypeChecker.funcStack.pop
                      (varPat.label.getText, res)
                case _ =>
                  ErrorManager.registerError(ERROR_UNEXPECTED_PATTERN_FOR_TYPE(
                    it.pattern_.getText, expectedVariantType.toString))
                  (null, null)
              }
              if patternType == null then shouldNull = true
              else
                actualPatterns = actualPatterns :+ (patternLabel, patternType)

            // Check for exhaustiveness
            val allPatternsLabels = actualPatterns.map((l, t) => l)
            val typePatternsLabels = expectedVariantType.labelsMap.map((l, t) => l)
            if allPatternsLabels.toSet != typePatternsLabels.toSet then
              ErrorManager.registerError(ERROR_NONEXHAUSTIVE_MATCH_PATTERNS(caseCtx.expr().toString))
              null
            else
              if shouldNull then null
              else
                // Finally, check if all the types are the same
                actualPatterns.find(p => !TypeChecker.validate(expr.getText, p._2, expectedType)) match {
                  case Some(p) =>
                    null
                  case _ =>
                    actualPatterns.head._2
                }
          case null => null
          case _ =>
            var shouldNull = false
            var actualPatterns = List.empty[(String, Type)]
            // Check all pattern types are same as expected type

            // First, collect all the types from pattern list
            for (it <- caseCtx.cases.asScala.toList)
              val (patternLabel, patternType) = it.pattern_ match {
                case varPat: StellaParser.PatternVarContext =>
                  if varPat.name == null then (null, null)
                  else
                    val scope = TypeChecker.funcStack.top
                    val expectedPatType = exprType
                    if expectedPatType == null then (null, null)
                    else
                      scope.addVariable(Variable(varPat.name.getText, expectedPatType))
                      TypeChecker.funcStack.push(scope)
                      val res = visitExpr(it.expr_, expectedType)
                      TypeChecker.funcStack.pop
                      (varPat.name.getText, res)
                case _ =>
                  ErrorManager.registerError(ERROR_UNEXPECTED_PATTERN_FOR_TYPE(
                    it.pattern_.getText, exprType.toString))
                  (null, null)
              }
              if patternType == null then shouldNull = true
              else
                actualPatterns = actualPatterns :+ (patternLabel, patternType)

            if shouldNull then null
            else
              // Finally, check if all the types are the same
              actualPatterns.find(p => !TypeChecker.validate(expr.getText, p._2, expectedType)) match {
                case Some(p) =>
                  null
                case _ =>
                  actualPatterns.head._2
              }
        }

      // Lists
      case consCtx: StellaParser.ConsListContext =>

      //   Г |- t_1 : T   Г |- t_2 : List[T]
      // ----------------------------------------- T-Cons
      //    Г |- cons[T] t_1 t_2 : List[T]

        expectedType match {
          case expectedListType: ListType =>
            val headType = visitExpr(consCtx.head, null)
            if headType == null then null
            else
              if !TypeChecker.validate(consCtx.head.toString(), headType, expectedListType.listType) then null
              else
                val resultType = ListType(headType)
                val tailType = visitExpr(consCtx.tail, resultType)
                if tailType == null then null
                else
                  if !TypeChecker.validate(consCtx.tail.getText, tailType, resultType) then null
                  else
                    resultType
          case null =>
            val headType = visitExpr(consCtx.head, null)

            val resultType = ListType(headType)
            val tailType = visitExpr(consCtx.tail, resultType)

            if headType == null then
              if tailType == null then
                if TypeChecker.isAmbiguousTypeAsBottom then return BotType
                ErrorManager.registerError(ERROR_AMBIGUOUS_LIST(expr.getText))
              null
            else
              if tailType == null then null
              else
                resultType
          case TopType => TopType
          case _ =>
            val actualType = visitExpr(consCtx, null) match {
              case t: Any => t
              case null => return null
            }
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
            if !TypeChecker.validate(headCtx.list.getText, expectedListType.listType, expectedType) then null
            else
              expectedListType.listType
          case null => null
          case BotType => BotType
          case _ =>
            if expectedType == null then return null
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
            if TypeChecker.validate(tailCtx.list.getText, tailListType, expectedType) then tailListType
            else
              null
          case null => null
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
              if TypeChecker.isAmbiguousTypeAsBottom then return BotType
              ErrorManager.registerError(ERROR_AMBIGUOUS_LIST("[]"))
              null
            else
              val exprTypes = exprScalaList.map( innerExpr =>
                {visitExpr(innerExpr, null)} )
              if exprTypes.contains(null) then null
              else
                ListType(exprTypes.head)
          case _ =>
            val actualType = visitExpr(listCtx, null) match {
              case t: Any => t
              case null => return null
            }
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
            if TypeChecker.validate(fixCtx.expr.getText, exprType, expectedFunctionType) then exprType.returnType
            else
              null

      // Variants
      case variantCtx: StellaParser.VariantContext =>

      //                                   Г |- t_j : T_j
      // ---------------------------------------------------------------------------------- T-Variant
      //    Г |- <l_j = t_j> as <l_1 : T_1, ..., l_n : T_n> : <l_1 : T_1, ..., l_n : T_n>

        expectedType match {
          case expectedVariantType: VariantType =>
            // Need to find type from expectedType by label
            val label = variantCtx.label.getText
            // Check if it is from expectedVariantType
            val labelType = expectedVariantType.labelsMap.find((l, t) => l == label) match {
              case Some(v) => v._2
              case _ =>
                ErrorManager.registerError(ERROR_UNEXPECTED_VARIANT_LABEL(label, expectedVariantType.toString))
                return null
            }
            val rhsTy = visitExpr(variantCtx.rhs, labelType)
            if rhsTy == null then null
            else
              if !TypeChecker.validate(variantCtx.rhs.getText, rhsTy, labelType) then null
              else
                expectedType
          case null =>
            if TypeChecker.isSubtypingEnabled then
              val rhsType = visitExpr(variantCtx.rhs, null)
              if rhsType != null then
                return VariantType(List((variantCtx.label.toString, rhsType)))
            if TypeChecker.isAmbiguousTypeAsBottom then return BotType
            ErrorManager.registerError(ERROR_AMBIGUOUS_VARIANT_TYPE(variantCtx.expr().getText))
            null
          case _ =>
            val actualType = visitExpr(variantCtx.expr(), null) match {
              case t: Any => t
              case null => return null
            }
            ErrorManager.registerError(ERROR_UNEXPECTED_VARIANT(expectedType.toString, actualType.toString))
            null
        }

      // References
      case refCtx: StellaParser.RefContext =>

      //        Г |- t_1 : T_1
      // ---------------------------- T-Ref
      //    Г |- ref t_1 : Ref T_1

        val innerExpectedType = expectedType match {
          case refType: ReferenceType => refType.innerType
          case _ => null
        }
        val expectedRefType = if innerExpectedType == null then null else ReferenceType(innerExpectedType)
        val innerExprType = visitExpr(refCtx.expr_, innerExpectedType)
        if innerExprType == null then null
        else
          if TypeChecker.validate(refCtx.getText, ReferenceType(innerExprType), expectedRefType) then ReferenceType(innerExprType)
          else
            null

      case derefCtx: StellaParser.DerefContext =>

      //      Г |- t_1 : Ref T_1
      // ---------------------------- T-Deref
      //    Г |- !t_1 : T_1

        val expectedRefType = if expectedType == null || expectedType == TopType then expectedType
                              else
                                ReferenceType(expectedType)
        visitExpr(derefCtx.expr_, expectedRefType) match {
          case refType: ReferenceType =>
            if TypeChecker.validate(derefCtx.expr_.getText, refType.innerType, expectedType) then refType.innerType
            else
              null
          case null => null
          // Ignoring BotType: decided to raise error (see in TG chat)
          case _ =>
            expectedRefType match {
              case t: Any =>
              case null => return null
            }
            ErrorManager.registerError(ERROR_NOT_A_REFERENCE(derefCtx.expr_.getText))
            null
        }

      case assignCtx: StellaParser.AssignContext =>

      //    Г |- t_1 : Ref T_1   Г |- t_2 : T_1
      // ----------------------------------------- T-Assign
      //           Г |- t_1 := t_2 : Unit

        expectedType match {
          case UnitType =>
            visitExpr(assignCtx.lhs, null) match {
              case lhsType: ReferenceType =>
                val rhsType = visitExpr(assignCtx.rhs, null)
                if rhsType == null then null
                else
                  if TypeChecker.validate(assignCtx.rhs.getText, lhsType.innerType, rhsType) then UnitType
                  else
                    null
              case null => null
              case _ =>
                ErrorManager.registerError(ERROR_NOT_A_REFERENCE(assignCtx.lhs.getText))
                null
            }
          case null => null
          case _ => null
        }

      // Constant memory
      case constMemCtx: StellaParser.ConstMemoryContext =>

      // No rule for const memory, so...
      //
      // -------------- T-ConstMem
      //    <n> : &T
      // As far as I understand, all we can do here is to take expectedType

        expectedType match {
          case refType: ReferenceType => refType
          case null =>
            if TypeChecker.isAmbiguousTypeAsBottom then BotType
            else
              ErrorManager.registerError(ERROR_AMBIGUOUS_REFERENCE_TYPE(expr.getText))
              null
          case TopType => TopType
          case _ =>
            ErrorManager.registerError(ERROR_UNEXPECTED_MEMORY_ADDRESS(
              constMemCtx.toString, expectedType.toString))
            null
        }

      // Panics
      case panicCtx: StellaParser.PanicContext =>

      //
      // ------------------ T-Error
      //   Г |- error : T

        if expectedType == null then
          if TypeChecker.isAmbiguousTypeAsBottom then BotType
          else
            ErrorManager.registerError(ERROR_AMBIGUOUS_PANIC_TYPE(expr.getText))
            null
        else
          expectedType

      // Exceptions
      case throwCtx: StellaParser.ThrowContext =>

      //    Г |- t_1 : T_exn
      // ---------------------- T-Raise
      //   Г |- throw t_1 : T

        if expectedType == null then
          if TypeChecker.isAmbiguousTypeAsBottom then BotType
          else
            ErrorManager.registerError(ERROR_AMBIGUOUS_THROW_TYPE(throwCtx.expr_.getText))
            null
        else
          if TypeChecker.exceptionType == null then
            ErrorManager.registerError(ERROR_EXCEPTION_TYPE_NOT_DECLARED())
            null
          else
            val innerExceptionType = visitExpr(throwCtx.expr_, TypeChecker.exceptionType)
            if innerExceptionType == null then null
            else
              if !TypeChecker.validate(throwCtx.expr_.getText, innerExceptionType, TypeChecker.exceptionType) then null
              else
                expectedType

      case tryWithCtx: StellaParser.TryWithContext =>

      //    Г |- t_1 : T   Г |- t_2 : T
      // ---------------------------------- T-Try
      //     Г |- try t_1 with t_2 : T

        if TypeChecker.exceptionType == null then
          ErrorManager.registerError(ERROR_EXCEPTION_TYPE_NOT_DECLARED())
          null
        else
          val tryType = visitExpr(tryWithCtx.tryExpr, expectedType)
          if tryType == null then null
          else
            if visitExpr(tryWithCtx.fallbackExpr, tryType) == null then null
            else
              if TypeChecker.validate(tryWithCtx.tryExpr.getText, tryType, expectedType) then tryType
              else
                null

      case tryCatchCtx: StellaParser.TryCatchContext =>

      //    Г |- t_1 : T   Г, x : T_exn |- t_2 : T
      // -------------------------------------------- T-Try
      //       Г |- try t_1 catch x => t_2 : T

        if TypeChecker.exceptionType == null then
          ErrorManager.registerError(ERROR_EXCEPTION_TYPE_NOT_DECLARED())
          null
        else
          val tryType = visitExpr(tryCatchCtx.tryExpr, expectedType)
          if tryType == null then null
          else
            val scope = TypeChecker.funcStack.top
            scope.addVariable(Variable(tryCatchCtx.pat.getText, TypeChecker.exceptionType))
            TypeChecker.funcStack.push(scope)
            if visitExpr(tryCatchCtx.fallbackExpr, tryType) == null then null
            else
              if TypeChecker.validate(tryCatchCtx.tryExpr.getText, tryType, expectedType) then tryType
              else
                null

      case _ =>
        null
    }
  }
}