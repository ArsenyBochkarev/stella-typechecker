package Stella

import Stella.Types.{FunctionType, *}
import Stella.Error.StellaError.*
import Stella.Error.*
import Stella.Unification.UnificationResult.{UNIFICATION_ERROR_FAILED, UNIFICATION_ERROR_INFINITE_TYPE, UNIFICATION_OK}
import Stella.Unification.{Constraint, Solver}

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
          case genericFunDecl: StellaParser.DeclFunGenericContext =>
            if genericFunDecl.name.getText == "main" then mainFound = true
            if visitDeclFunGeneric(genericFunDecl) == null then
              break()
            else println(s"Function ${genericFunDecl.name.getText} processed")
          case _ =>
            println("Ignored non-function declaration")
        }
      }
    if TypeChecker.isTypeReconstructionEnabled then
      Solver.solve() match {
        case res: UNIFICATION_ERROR_FAILED =>
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(res.expr, res.expectedType, res.actualType))
        case res: UNIFICATION_ERROR_INFINITE_TYPE =>
          ErrorManager.registerError(ERROR_OCCURS_CHECK_INFINITE_TYPE(res.expr))
        case _ =>
      }
    ErrorManager.outputErrors()
  }

  private def inspectExtensions(ctx: StellaParser.ProgramContext): Unit = {
    ctx.extensions.asScala.foreach {
      case extInstance: StellaParser.AnExtensionContext =>
        extInstance.ExtensionName.getText match
          case "#type-reconstruction" => TypeChecker.isTypeReconstructionEnabled = true
          case _ =>
      case _ =>
    }
  }

  override def visitDeclFunGeneric(ctx: StellaParser.DeclFunGenericContext): Any = {
    val typeContext: VarContext = if TypeChecker.funcStack.nonEmpty then TypeChecker.funcStack.top else VarContext()
    ctx.generics.forEach( typeVar => {
      typeContext.addTypeVariable(Variable(typeVar.getText, GenericType(typeVar.getText)))
    })
    functionsContext.addTypeVariables(
      ctx.name.getText,
      ctx.generics.asScala.map( typeVar => {
        GenericType(typeVar.getText)
      }).toList
    )

    val arg = ctx.paramDecls.get(0)
    val argType: Type = TypeChecker.ctxToType(arg.paramType)
    typeContext.addVariable(Variable(varStr = arg.name.getText, argType))
    TypeChecker.funcStack.push(typeContext)

    val expectedReturnType: Type = TypeChecker.ctxToType(ctx.returnType)
    if expectedReturnType == null then return null

    val ty = FunctionType(retType = expectedReturnType, argumentType = argType)
    val refinedTy = UniversalType(ty, ctx.generics.asScala.map( typeVar => { GenericType(typeVar.getText) }).toList)
    functionsContext.addFunction(ctx.name.getText, refinedTy)
    val res = visitExpr(ctx.returnExpr, expectedReturnType)
    TypeChecker.funcStack.pop()
    res
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
      case constIntCtx: StellaParser.ConstIntContext => if (TypeChecker.validate(NatType, expectedType, constIntCtx.getText)) NatType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(constIntCtx.n.getText,
          NatType.toString(), expectedType.toString()))
        null
      case constTrueCtx: StellaParser.ConstTrueContext => if (TypeChecker.validate(BoolType, expectedType, constTrueCtx.getText)) BoolType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText,
          BoolType.toString(), expectedType.toString()))
        null
      case _: StellaParser.ConstFalseContext => if (TypeChecker.validate(BoolType, expectedType, expr.getText)) BoolType
      else
        ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText,
          BoolType.toString(), expectedType.toString()))
        null
      case exprCtx: StellaParser.ConstUnitContext => if (TypeChecker.validate(UnitType, expectedType, exprCtx.getText)) UnitType
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
            // Try to match the function type
            functionsContext.functionTypes.get(varCtx.name.getText) match {
              case Some(v) => v
              case _ =>
                ErrorManager.registerError(ERROR_UNDEFINED_VARIABLE(varCtx.name.getText))
                null
            }
        }
        if varType == null then null
        else
          if TypeChecker.validate(varType, expectedType, varCtx.getText) then varType
          else
            ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
              varCtx.getText, varType.toString, expectedType.toString))
            null

      // If
      case ifCtx: StellaParser.IfContext => // expr == type(expr), cond == Bool, type(then) == type(else) == expectedType
        val condType = visitExpr(ifCtx.condition, BoolType)
        if condType == null then return null
        val thenType = visitExpr(ifCtx.thenExpr, expectedType)
        val elseType = visitExpr(ifCtx.elseExpr, expectedType)
        if thenType == null || TypeChecker.validate(thenType, elseType, ifCtx.getText) then thenType
        else
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
            ifCtx.thenExpr.getText, thenType.toString(), elseType.toString()))
          null

      // Application
      case appCtx: StellaParser.ApplicationContext =>

      //   Г |- t_1 : T_1 -> T_2   Г |- t_2 : T_1
      // ------------------------------------------ T-App
      //              Г |- t_1 t_2 : T_2

        // It looks like we shouldn't match UniversalType here, because their type (if correct)
        // will be evaluated anyway in visitExpr(appCtx.fun, null) (since we cannot do
        // application for non-instantiated generic type)
        val tmpTy: Type = functionsContext.functionTypes.get(appCtx.fun.getText) match {
          case Some(foundType: FunctionType) => foundType
          case _ =>
            TypeChecker.funcStack.top.varTypes.get(appCtx.fun.getText) match {
              case Some(foundType: FunctionType) => foundType
              case Some(foundTypeVar: TypeVar) => foundTypeVar
              case Some(otherType) => // Some other type, error
                ErrorManager.registerError(ERROR_NOT_A_FUNCTION(appCtx.fun.getText, expectedType.toString()))
                return null
              case _ =>
                visitExpr(appCtx.fun, null) match {
                  case fTy: FunctionType => fTy
                  case tyVar: TypeVar => tyVar
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

        if TypeChecker.isTypeReconstructionEnabled then

          //    Г |- t_1 : T_1 | C_1   Г |- t_2 : T_2 | C_2   X -- fresh TypeVar
          // ---------------------------------------------------------------------- СT-App
          //              Г |- t_1 t_2 : X | (C_1 ∪ C_2 ∪ {T_1 = T_2 -> X})

          if argType == null then return null
          val res = if expectedType == null then TypeVarWrapper.createTypeVar() else expectedType // Optimize a bit
          Solver.addConstraint(Constraint(tmpTy,
            FunctionType(argType, res),
            appCtx.getText))
          return res

        val funcType: FunctionType = tmpTy match
          case fTy: FunctionType => fTy
          case _ => return null
        if !TypeChecker.validate(funcType.argType, argType, appCtx.getText) then
          ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr = appCtx.args.get(0).getText,
            funcType.argType.toString(), argType.toString()))
          return null
        if TypeChecker.validate(funcType.returnType, expectedType, appCtx.getText) then
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
          case _: TypeVar =>
          case null =>
          case _ =>
            val actualType = visitExpr(absCtx.expr(), null) match {
              case t: Type => t
              case null => return null
            }
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
          case typeVar: TypeVar =>
            val retType = TypeVarWrapper.createTypeVar()
            val expectedFuncType = FunctionType(argType, retType)
            Solver.addConstraint(Constraint(expectedType, expectedFuncType, absCtx.expr().getText))
            val res = visitExpr(absCtx.returnExpr, retType)

            if res == null then null
            else
              FunctionType(argType, res)
          case null =>
            val retType = visitExpr(absCtx.returnExpr, null)
            if retType == null then null
            else
              FunctionType(argType, retType)
        }

      case natRecCtx: StellaParser.NatRecContext =>

      //    Г |- t_1 : Nat   Г |- t_2 : C   Г |- t_3 : Nat -> C -> C
      // -------------------------------------------------------------- T-RecNat
      //                   Г |- rec(t_1, t_2, t_3) : C

        if visitExpr(natRecCtx.n, NatType) == null then null
        else
          val initialType = visitExpr(natRecCtx.initial, expectedType)
          if initialType == null then null
          else
            val expectedStepFunctionType = FunctionType(NatType, FunctionType(expectedType, expectedType))
            if visitExpr(natRecCtx.step, expectedStepFunctionType) == null then null
            else
              initialType

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

        val correctedExpectedType = TypeChecker.ctxToType(ascCtx.type_)
        val innerType = visitExpr(ascCtx.expr_, if expectedType == null then correctedExpectedType else expectedType)
        if innerType == null then null
        else
            val ctxType = TypeChecker.ctxToType(ascCtx.type_)
            if TypeChecker.validate(ctxType, expectedType, ascCtx.getText) then ctxType
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
              if TypeChecker.validate(elemTy, expectedType, dotTupleCtx.getText) then elemTy
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
            if TypeChecker.validate(recTy, expectedType, recordCtx.getText) then
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
            // Check if it is from recordType
            recordType.labelsMap.find((l, t) => l == label) match {
              case Some(v) =>
                if TypeChecker.validate(v._2, expectedType, dotRecordCtx.getText) then v._2
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
            val actualType = visitExpr(inlCtx.expr(), expectedType) match {
              case t: Any => t
              case null => return null
            }
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
            val actualType = visitExpr(inrCtx.expr(), expectedType) match {
              case t: Any => t
              case null => return null
            }
            ErrorManager.registerError(
              ERROR_UNEXPECTED_INJECTION(inrCtx.expr().getText, actualType.toString))
            null
        }
      case semicolonCtx: StellaParser.TerminatingSemicolonContext =>
        visitExpr(semicolonCtx.expr_, expectedType)
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
                patternTypes.find(p =>
                  !TypeChecker.validate(p, expectedType, caseCtx.getText)) match {
                  case Some(p) =>
                    ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText, p.toString, expectedType.toString))
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
                actualPatterns.find(p =>
                  !TypeChecker.validate(p._2, expectedType, caseCtx.getText)) match {
                  case Some(p) =>
                    ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr.getText, p.toString, expectedType.toString))
                    null
                  case _ =>
                    actualPatterns.head._2
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
              if expectedType != null && !TypeChecker.validate(headType, expectedListType.listType, consCtx.getText) then
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
            if TypeChecker.validate(expectedListType.listType, expectedType, headCtx.getText) then expectedListType
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
            if TypeChecker.validate(tailListType, expectedType, tailCtx.getText) then tailListType
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
            if TypeChecker.validate(exprType, expectedFunctionType, fixCtx.getText) then
              exprType.returnType
            else
              ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
                fixCtx.expr.getText, exprType.toString, expectedFunctionType.toString))
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
            if visitExpr(variantCtx.rhs, labelType) == null then null
            else
              expectedType
          case null =>
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

      case typeAppCtx: StellaParser.TypeApplicationContext =>

      //     Г |- t : forall X. T
      // ---------------------------- T-TApp
      //   Г |- t [S] : [X |-> S] T

        visitExpr(typeAppCtx.fun, null) match {
          case univTy: UniversalType =>
            univTy.innerType match {
              case funcTy: FunctionType =>
                // Comparing number of saved generics with actual one
                val expectedNumber = univTy.outerTypes.size
                val actualTypes = typeAppCtx.types.asScala.map(ty => {
                  TypeChecker.ctxToType(ty)
                })
                if expectedNumber != actualTypes.size then
                  ErrorManager.registerError(ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS(
                    typeAppCtx.fun.getText, expectedNumber, actualTypes.size
                  ))
                  null
                else
                  // Substitute actual types to univTy
                  val subst = univTy.outerTypes.zip(actualTypes).toMap
                  val actualType = funcTy.substituteTypes(subst)
                  TypeChecker.checkUnresolvedType(actualType, TypeChecker.funcStack) match {
                    case t: Type =>
                      ErrorManager.registerError(ERROR_UNDEFINED_TYPE_VARIABLE(t.toString))
                      return null
                    case null =>
                  }
                  if TypeChecker.validate(actualType, expectedType, typeAppCtx.getText) then
                    actualType
                  else
                    null
              case _ =>
                ErrorManager.registerError(ERROR_NOT_A_GENERIC_FUNCTION(typeAppCtx.fun.getText, expectedType.toString))
                null
            }
          case _ =>
            expectedType match {
              case t: Any => ErrorManager.registerError(ERROR_NOT_A_GENERIC_FUNCTION(typeAppCtx.fun.getText, expectedType.toString))
              case null =>
            }
            null
        }
      case typeAbsCtx: StellaParser.TypeAbstractionContext =>

      //     Г, X |- t : T
      // ---------------------------- T-TAbs
      //   Г |- ΛX.t : forall X.T

        expectedType match {
          case univTy: UniversalType =>
            val tc = TypeChecker.funcStack.top
            typeAbsCtx.generics.forEach( typeVar => {
              tc.addTypeVariable(Variable(typeVar.getText, GenericType(typeVar.getText)))
            })
            val typeParams = typeAbsCtx.generics.asScala.map( g => { GenericType(g.getText) } ).toList
            val actualType = visitExpr(typeAbsCtx.expr_, univTy.innerType)
            if actualType == null then null
            else
              UniversalType(actualType, typeParams)
          case _ =>
            val actualType = visitExpr(typeAbsCtx.expr_, null)
            if actualType == null || expectedType == null then null
            else
              ErrorManager.registerError(
                ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(typeAbsCtx.expr_.getText, actualType.toString, expectedType.toString)
              )
              null
        }
      case _ =>
        null
    }
  }
}