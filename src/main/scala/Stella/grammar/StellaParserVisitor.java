// Generated from StellaParser.g4 by ANTLR 4.13.2
package Stella;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link StellaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface StellaParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link StellaParser#start_Program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_Program(StellaParser.Start_ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#start_Expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_Expr(StellaParser.Start_ExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#start_Type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart_Type(StellaParser.Start_TypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(StellaParser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LanguageCore}
	 * labeled alternative in {@link StellaParser#languageDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLanguageCore(StellaParser.LanguageCoreContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AnExtension}
	 * labeled alternative in {@link StellaParser#extension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnExtension(StellaParser.AnExtensionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclFun}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclFun(StellaParser.DeclFunContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclFunGeneric}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclFunGeneric(StellaParser.DeclFunGenericContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclTypeAlias}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclTypeAlias(StellaParser.DeclTypeAliasContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclExceptionType}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclExceptionType(StellaParser.DeclExceptionTypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DeclExceptionVariant}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclExceptionVariant(StellaParser.DeclExceptionVariantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code InlineAnnotation}
	 * labeled alternative in {@link StellaParser#annotation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInlineAnnotation(StellaParser.InlineAnnotationContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#paramDecl}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParamDecl(StellaParser.ParamDeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Fold}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFold(StellaParser.FoldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(StellaParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IsZero}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsZero(StellaParser.IsZeroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVar(StellaParser.VarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeAbstraction}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAbstraction(StellaParser.TypeAbstractionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Divide}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivide(StellaParser.DivideContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThan(StellaParser.LessThanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotRecord}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotRecord(StellaParser.DotRecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreaterThan}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThan(StellaParser.GreaterThanContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqual(StellaParser.EqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Throw}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitThrow(StellaParser.ThrowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Multiply}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiply(StellaParser.MultiplyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstMemory}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstMemory(StellaParser.ConstMemoryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code List}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(StellaParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryCatch}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCatch(StellaParser.TryCatchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryCastAs}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryCastAs(StellaParser.TryCastAsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Head}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHead(StellaParser.HeadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TerminatingSemicolon}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerminatingSemicolon(StellaParser.TerminatingSemicolonContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotEqual(StellaParser.NotEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstUnit}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstUnit(StellaParser.ConstUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Sequence}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSequence(StellaParser.SequenceContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstFalse}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstFalse(StellaParser.ConstFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAbstraction(StellaParser.AbstractionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstInt}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstInt(StellaParser.ConstIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variant}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariant(StellaParser.VariantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConstTrue}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstTrue(StellaParser.ConstTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Subtract}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubtract(StellaParser.SubtractContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeCast}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCast(StellaParser.TypeCastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code If}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(StellaParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Application}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitApplication(StellaParser.ApplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Deref}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeref(StellaParser.DerefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code IsEmpty}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIsEmpty(StellaParser.IsEmptyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Panic}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPanic(StellaParser.PanicContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LessThanOrEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLessThanOrEqual(StellaParser.LessThanOrEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Succ}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSucc(StellaParser.SuccContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Inl}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInl(StellaParser.InlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code GreaterThanOrEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGreaterThanOrEqual(StellaParser.GreaterThanOrEqualContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Inr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInr(StellaParser.InrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Match}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch(StellaParser.MatchContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicNot}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicNot(StellaParser.LogicNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesisedExpr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisedExpr(StellaParser.ParenthesisedExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Tail}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTail(StellaParser.TailContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Record}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecord(StellaParser.RecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicAnd}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicAnd(StellaParser.LogicAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeApplication}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeApplication(StellaParser.TypeApplicationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LetRec}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLetRec(StellaParser.LetRecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code LogicOr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicOr(StellaParser.LogicOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TryWith}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTryWith(StellaParser.TryWithContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Pred}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPred(StellaParser.PredContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeAsc}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAsc(StellaParser.TypeAscContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NatRec}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNatRec(StellaParser.NatRecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Unfold}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnfold(StellaParser.UnfoldContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Ref}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef(StellaParser.RefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code DotTuple}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDotTuple(StellaParser.DotTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Fix}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFix(StellaParser.FixContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Let}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLet(StellaParser.LetContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(StellaParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Tuple}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTuple(StellaParser.TupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ConsList}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConsList(StellaParser.ConsListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#patternBinding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternBinding(StellaParser.PatternBindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#binding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding(StellaParser.BindingContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#matchCase}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchCase(StellaParser.MatchCaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternCons}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternCons(StellaParser.PatternConsContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternTuple}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternTuple(StellaParser.PatternTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternList}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternList(StellaParser.PatternListContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternRecord}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternRecord(StellaParser.PatternRecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternVariant}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternVariant(StellaParser.PatternVariantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternAsc}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternAsc(StellaParser.PatternAscContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternInt}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternInt(StellaParser.PatternIntContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternInr}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternInr(StellaParser.PatternInrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternTrue}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternTrue(StellaParser.PatternTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternInl}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternInl(StellaParser.PatternInlContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternVar}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternVar(StellaParser.PatternVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesisedPattern}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesisedPattern(StellaParser.ParenthesisedPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternSucc}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternSucc(StellaParser.PatternSuccContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternFalse}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternFalse(StellaParser.PatternFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternUnit}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternUnit(StellaParser.PatternUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PatternCastAs}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPatternCastAs(StellaParser.PatternCastAsContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#labelledPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLabelledPattern(StellaParser.LabelledPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeTuple}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeTuple(StellaParser.TypeTupleContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeTop}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeTop(StellaParser.TypeTopContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeBool}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBool(StellaParser.TypeBoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeRef}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRef(StellaParser.TypeRefContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeRec}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRec(StellaParser.TypeRecContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeAuto}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeAuto(StellaParser.TypeAutoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeSum}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeSum(StellaParser.TypeSumContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeVar}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeVar(StellaParser.TypeVarContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeVariant}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeVariant(StellaParser.TypeVariantContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeUnit}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeUnit(StellaParser.TypeUnitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeNat}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeNat(StellaParser.TypeNatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeBottom}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeBottom(StellaParser.TypeBottomContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeParens}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeParens(StellaParser.TypeParensContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeFun}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeFun(StellaParser.TypeFunContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeForAll}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeForAll(StellaParser.TypeForAllContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeRecord}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeRecord(StellaParser.TypeRecordContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TypeList}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeList(StellaParser.TypeListContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#recordFieldType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRecordFieldType(StellaParser.RecordFieldTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link StellaParser#variantFieldType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariantFieldType(StellaParser.VariantFieldTypeContext ctx);
}