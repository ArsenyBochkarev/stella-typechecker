package Stella

class StellaVisitor extends StellaParserBaseVisitor[Any] {
  override def visitProgram(ctx: StellaParser.ProgramContext): Any = {
    visitChildren(ctx)
    println("Program processed")
  }

  override def visitDeclFun(ctx: StellaParser.DeclFunContext): Any = {
    println(s"Function decl visited !! Name: ${ctx.name.getText}\n\n")
  }
}