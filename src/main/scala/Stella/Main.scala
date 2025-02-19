package Stella

import Stella.grammar.*
import org.antlr.v4.runtime.*

import scala.jdk.CollectionConverters
import scala.util.Try

def parse(input: String): Unit = {
  println("\nevaluating expression " + input)

  val charstream = CharStreams.fromFileName(input)
  val lexer = new StellaLexer(charstream)
  val tokens = new CommonTokenStream(lexer)
  val parser = new StellaParser(tokens)

  /* implement listener and use parser */
}

object Main extends App {
  parse("/home/mexanobar/programming/Edu/ITMO/2_semester/type_systems/stella-typechecker-scala/untitled/src/test/scala/simple.stella")
  println("parsed!")
}

