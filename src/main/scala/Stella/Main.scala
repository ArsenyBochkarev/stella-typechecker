package Stella

import Stella.Grammar.*
import org.antlr.v4.runtime.*

import scala.jdk.CollectionConverters
import scala.util.Try

def parse(input: String): Unit = {
  val charstream = CharStreams.fromFileName(input)
  val lexer = new StellaLexer(charstream)
  val tokens = new CommonTokenStream(lexer)
  val parser = new StellaParser(tokens)

  val tree = parser.program()
  val visitor = new StellaVisitor()
  visitor.visit(tree)
}

@main
def main(filePath: String): Unit = {
    parse("./src/test/scala/sum_simple.stella")
//  parse(filePath)
    System.exit(OutputManager.getOutput)
}

