// Generated from StellaParser.g4 by ANTLR 4.13.2
package Stella;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link StellaParser}.
 */
public interface StellaParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link StellaParser#start_Program}.
	 * @param ctx the parse tree
	 */
	void enterStart_Program(StellaParser.Start_ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#start_Program}.
	 * @param ctx the parse tree
	 */
	void exitStart_Program(StellaParser.Start_ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#start_Expr}.
	 * @param ctx the parse tree
	 */
	void enterStart_Expr(StellaParser.Start_ExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#start_Expr}.
	 * @param ctx the parse tree
	 */
	void exitStart_Expr(StellaParser.Start_ExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#start_Type}.
	 * @param ctx the parse tree
	 */
	void enterStart_Type(StellaParser.Start_TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#start_Type}.
	 * @param ctx the parse tree
	 */
	void exitStart_Type(StellaParser.Start_TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(StellaParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(StellaParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LanguageCore}
	 * labeled alternative in {@link StellaParser#languageDecl}.
	 * @param ctx the parse tree
	 */
	void enterLanguageCore(StellaParser.LanguageCoreContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LanguageCore}
	 * labeled alternative in {@link StellaParser#languageDecl}.
	 * @param ctx the parse tree
	 */
	void exitLanguageCore(StellaParser.LanguageCoreContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AnExtension}
	 * labeled alternative in {@link StellaParser#extension}.
	 * @param ctx the parse tree
	 */
	void enterAnExtension(StellaParser.AnExtensionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AnExtension}
	 * labeled alternative in {@link StellaParser#extension}.
	 * @param ctx the parse tree
	 */
	void exitAnExtension(StellaParser.AnExtensionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclFun}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDeclFun(StellaParser.DeclFunContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclFun}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDeclFun(StellaParser.DeclFunContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclFunGeneric}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDeclFunGeneric(StellaParser.DeclFunGenericContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclFunGeneric}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDeclFunGeneric(StellaParser.DeclFunGenericContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclTypeAlias}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDeclTypeAlias(StellaParser.DeclTypeAliasContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclTypeAlias}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDeclTypeAlias(StellaParser.DeclTypeAliasContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclExceptionType}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDeclExceptionType(StellaParser.DeclExceptionTypeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclExceptionType}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDeclExceptionType(StellaParser.DeclExceptionTypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DeclExceptionVariant}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void enterDeclExceptionVariant(StellaParser.DeclExceptionVariantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DeclExceptionVariant}
	 * labeled alternative in {@link StellaParser#decl}.
	 * @param ctx the parse tree
	 */
	void exitDeclExceptionVariant(StellaParser.DeclExceptionVariantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code InlineAnnotation}
	 * labeled alternative in {@link StellaParser#annotation}.
	 * @param ctx the parse tree
	 */
	void enterInlineAnnotation(StellaParser.InlineAnnotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code InlineAnnotation}
	 * labeled alternative in {@link StellaParser#annotation}.
	 * @param ctx the parse tree
	 */
	void exitInlineAnnotation(StellaParser.InlineAnnotationContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#paramDecl}.
	 * @param ctx the parse tree
	 */
	void enterParamDecl(StellaParser.ParamDeclContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#paramDecl}.
	 * @param ctx the parse tree
	 */
	void exitParamDecl(StellaParser.ParamDeclContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Fold}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFold(StellaParser.FoldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Fold}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFold(StellaParser.FoldContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Add}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAdd(StellaParser.AddContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAdd(StellaParser.AddContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsZero}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIsZero(StellaParser.IsZeroContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsZero}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIsZero(StellaParser.IsZeroContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Var}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVar(StellaParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Var}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVar(StellaParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeAbstraction}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTypeAbstraction(StellaParser.TypeAbstractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeAbstraction}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTypeAbstraction(StellaParser.TypeAbstractionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Divide}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDivide(StellaParser.DivideContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Divide}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDivide(StellaParser.DivideContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLessThan(StellaParser.LessThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThan}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLessThan(StellaParser.LessThanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotRecord}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDotRecord(StellaParser.DotRecordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotRecord}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDotRecord(StellaParser.DotRecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThan}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThan(StellaParser.GreaterThanContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThan}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThan(StellaParser.GreaterThanContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterEqual(StellaParser.EqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Equal}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitEqual(StellaParser.EqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Throw}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterThrow(StellaParser.ThrowContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Throw}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitThrow(StellaParser.ThrowContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Multiply}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMultiply(StellaParser.MultiplyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Multiply}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMultiply(StellaParser.MultiplyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstMemory}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConstMemory(StellaParser.ConstMemoryContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstMemory}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConstMemory(StellaParser.ConstMemoryContext ctx);
	/**
	 * Enter a parse tree produced by the {@code List}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterList(StellaParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code List}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitList(StellaParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryCatch}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTryCatch(StellaParser.TryCatchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryCatch}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTryCatch(StellaParser.TryCatchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryCastAs}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTryCastAs(StellaParser.TryCastAsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryCastAs}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTryCastAs(StellaParser.TryCastAsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Head}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterHead(StellaParser.HeadContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Head}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitHead(StellaParser.HeadContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TerminatingSemicolon}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTerminatingSemicolon(StellaParser.TerminatingSemicolonContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TerminatingSemicolon}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTerminatingSemicolon(StellaParser.TerminatingSemicolonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNotEqual(StellaParser.NotEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NotEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNotEqual(StellaParser.NotEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstUnit}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConstUnit(StellaParser.ConstUnitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstUnit}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConstUnit(StellaParser.ConstUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Sequence}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSequence(StellaParser.SequenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Sequence}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSequence(StellaParser.SequenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstFalse}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConstFalse(StellaParser.ConstFalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstFalse}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConstFalse(StellaParser.ConstFalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAbstraction(StellaParser.AbstractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Abstraction}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAbstraction(StellaParser.AbstractionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstInt}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConstInt(StellaParser.ConstIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstInt}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConstInt(StellaParser.ConstIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variant}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterVariant(StellaParser.VariantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variant}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitVariant(StellaParser.VariantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConstTrue}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConstTrue(StellaParser.ConstTrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConstTrue}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConstTrue(StellaParser.ConstTrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Subtract}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSubtract(StellaParser.SubtractContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Subtract}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSubtract(StellaParser.SubtractContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeCast}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTypeCast(StellaParser.TypeCastContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeCast}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTypeCast(StellaParser.TypeCastContext ctx);
	/**
	 * Enter a parse tree produced by the {@code If}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIf(StellaParser.IfContext ctx);
	/**
	 * Exit a parse tree produced by the {@code If}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIf(StellaParser.IfContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Application}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterApplication(StellaParser.ApplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Application}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitApplication(StellaParser.ApplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Deref}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDeref(StellaParser.DerefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Deref}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDeref(StellaParser.DerefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code IsEmpty}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterIsEmpty(StellaParser.IsEmptyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code IsEmpty}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitIsEmpty(StellaParser.IsEmptyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Panic}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPanic(StellaParser.PanicContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Panic}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPanic(StellaParser.PanicContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LessThanOrEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLessThanOrEqual(StellaParser.LessThanOrEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LessThanOrEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLessThanOrEqual(StellaParser.LessThanOrEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Succ}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterSucc(StellaParser.SuccContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Succ}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitSucc(StellaParser.SuccContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Inl}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInl(StellaParser.InlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Inl}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInl(StellaParser.InlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code GreaterThanOrEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterGreaterThanOrEqual(StellaParser.GreaterThanOrEqualContext ctx);
	/**
	 * Exit a parse tree produced by the {@code GreaterThanOrEqual}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitGreaterThanOrEqual(StellaParser.GreaterThanOrEqualContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Inr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInr(StellaParser.InrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Inr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInr(StellaParser.InrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Match}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMatch(StellaParser.MatchContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Match}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMatch(StellaParser.MatchContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicNot}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicNot(StellaParser.LogicNotContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicNot}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicNot(StellaParser.LogicNotContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesisedExpr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisedExpr(StellaParser.ParenthesisedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesisedExpr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisedExpr(StellaParser.ParenthesisedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Tail}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTail(StellaParser.TailContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Tail}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTail(StellaParser.TailContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Record}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRecord(StellaParser.RecordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Record}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRecord(StellaParser.RecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicAnd}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicAnd(StellaParser.LogicAndContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicAnd}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicAnd(StellaParser.LogicAndContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeApplication}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTypeApplication(StellaParser.TypeApplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeApplication}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTypeApplication(StellaParser.TypeApplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LetRec}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLetRec(StellaParser.LetRecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LetRec}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLetRec(StellaParser.LetRecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code LogicOr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLogicOr(StellaParser.LogicOrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code LogicOr}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLogicOr(StellaParser.LogicOrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TryWith}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTryWith(StellaParser.TryWithContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TryWith}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTryWith(StellaParser.TryWithContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Pred}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterPred(StellaParser.PredContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Pred}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitPred(StellaParser.PredContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeAsc}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTypeAsc(StellaParser.TypeAscContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeAsc}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTypeAsc(StellaParser.TypeAscContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NatRec}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterNatRec(StellaParser.NatRecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NatRec}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitNatRec(StellaParser.NatRecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Unfold}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterUnfold(StellaParser.UnfoldContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Unfold}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitUnfold(StellaParser.UnfoldContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Ref}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterRef(StellaParser.RefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Ref}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitRef(StellaParser.RefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code DotTuple}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterDotTuple(StellaParser.DotTupleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code DotTuple}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitDotTuple(StellaParser.DotTupleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Fix}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterFix(StellaParser.FixContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Fix}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitFix(StellaParser.FixContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Let}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterLet(StellaParser.LetContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Let}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitLet(StellaParser.LetContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign(StellaParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Assign}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign(StellaParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Tuple}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterTuple(StellaParser.TupleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Tuple}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitTuple(StellaParser.TupleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ConsList}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterConsList(StellaParser.ConsListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ConsList}
	 * labeled alternative in {@link StellaParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitConsList(StellaParser.ConsListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#patternBinding}.
	 * @param ctx the parse tree
	 */
	void enterPatternBinding(StellaParser.PatternBindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#patternBinding}.
	 * @param ctx the parse tree
	 */
	void exitPatternBinding(StellaParser.PatternBindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#binding}.
	 * @param ctx the parse tree
	 */
	void enterBinding(StellaParser.BindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#binding}.
	 * @param ctx the parse tree
	 */
	void exitBinding(StellaParser.BindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#matchCase}.
	 * @param ctx the parse tree
	 */
	void enterMatchCase(StellaParser.MatchCaseContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#matchCase}.
	 * @param ctx the parse tree
	 */
	void exitMatchCase(StellaParser.MatchCaseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternCons}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternCons(StellaParser.PatternConsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternCons}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternCons(StellaParser.PatternConsContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternTuple}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternTuple(StellaParser.PatternTupleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternTuple}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternTuple(StellaParser.PatternTupleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternList}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternList(StellaParser.PatternListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternList}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternList(StellaParser.PatternListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternRecord}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternRecord(StellaParser.PatternRecordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternRecord}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternRecord(StellaParser.PatternRecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternVariant}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternVariant(StellaParser.PatternVariantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternVariant}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternVariant(StellaParser.PatternVariantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternAsc}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternAsc(StellaParser.PatternAscContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternAsc}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternAsc(StellaParser.PatternAscContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternInt}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternInt(StellaParser.PatternIntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternInt}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternInt(StellaParser.PatternIntContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternInr}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternInr(StellaParser.PatternInrContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternInr}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternInr(StellaParser.PatternInrContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternTrue}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternTrue(StellaParser.PatternTrueContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternTrue}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternTrue(StellaParser.PatternTrueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternInl}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternInl(StellaParser.PatternInlContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternInl}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternInl(StellaParser.PatternInlContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternVar}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternVar(StellaParser.PatternVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternVar}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternVar(StellaParser.PatternVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesisedPattern}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterParenthesisedPattern(StellaParser.ParenthesisedPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesisedPattern}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitParenthesisedPattern(StellaParser.ParenthesisedPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternSucc}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternSucc(StellaParser.PatternSuccContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternSucc}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternSucc(StellaParser.PatternSuccContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternFalse}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternFalse(StellaParser.PatternFalseContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternFalse}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternFalse(StellaParser.PatternFalseContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternUnit}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternUnit(StellaParser.PatternUnitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternUnit}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternUnit(StellaParser.PatternUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PatternCastAs}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void enterPatternCastAs(StellaParser.PatternCastAsContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PatternCastAs}
	 * labeled alternative in {@link StellaParser#pattern}.
	 * @param ctx the parse tree
	 */
	void exitPatternCastAs(StellaParser.PatternCastAsContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#labelledPattern}.
	 * @param ctx the parse tree
	 */
	void enterLabelledPattern(StellaParser.LabelledPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#labelledPattern}.
	 * @param ctx the parse tree
	 */
	void exitLabelledPattern(StellaParser.LabelledPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeTuple}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeTuple(StellaParser.TypeTupleContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeTuple}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeTuple(StellaParser.TypeTupleContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeTop}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeTop(StellaParser.TypeTopContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeTop}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeTop(StellaParser.TypeTopContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeBool}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeBool(StellaParser.TypeBoolContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeBool}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeBool(StellaParser.TypeBoolContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeRef}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeRef(StellaParser.TypeRefContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeRef}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeRef(StellaParser.TypeRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeRec}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeRec(StellaParser.TypeRecContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeRec}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeRec(StellaParser.TypeRecContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeAuto}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeAuto(StellaParser.TypeAutoContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeAuto}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeAuto(StellaParser.TypeAutoContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeSum}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeSum(StellaParser.TypeSumContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeSum}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeSum(StellaParser.TypeSumContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeVar}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeVar(StellaParser.TypeVarContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeVar}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeVar(StellaParser.TypeVarContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeVariant}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeVariant(StellaParser.TypeVariantContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeVariant}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeVariant(StellaParser.TypeVariantContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeUnit}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeUnit(StellaParser.TypeUnitContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeUnit}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeUnit(StellaParser.TypeUnitContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeNat}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeNat(StellaParser.TypeNatContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeNat}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeNat(StellaParser.TypeNatContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeBottom}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeBottom(StellaParser.TypeBottomContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeBottom}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeBottom(StellaParser.TypeBottomContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeParens}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeParens(StellaParser.TypeParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeParens}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeParens(StellaParser.TypeParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeFun}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeFun(StellaParser.TypeFunContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeFun}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeFun(StellaParser.TypeFunContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeForAll}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeForAll(StellaParser.TypeForAllContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeForAll}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeForAll(StellaParser.TypeForAllContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeRecord}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeRecord(StellaParser.TypeRecordContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeRecord}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeRecord(StellaParser.TypeRecordContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TypeList}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void enterTypeList(StellaParser.TypeListContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TypeList}
	 * labeled alternative in {@link StellaParser#stellatype}.
	 * @param ctx the parse tree
	 */
	void exitTypeList(StellaParser.TypeListContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#recordFieldType}.
	 * @param ctx the parse tree
	 */
	void enterRecordFieldType(StellaParser.RecordFieldTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#recordFieldType}.
	 * @param ctx the parse tree
	 */
	void exitRecordFieldType(StellaParser.RecordFieldTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link StellaParser#variantFieldType}.
	 * @param ctx the parse tree
	 */
	void enterVariantFieldType(StellaParser.VariantFieldTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link StellaParser#variantFieldType}.
	 * @param ctx the parse tree
	 */
	void exitVariantFieldType(StellaParser.VariantFieldTypeContext ctx);
}