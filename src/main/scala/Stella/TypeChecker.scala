package Stella

import scala.collection.mutable.Stack
import Stella.Types.*

import scala.collection.mutable

object TypeChecker {
  var funcStack: Stack[VarContext] = new mutable.Stack[VarContext]()

  def validate(actualType: Type, expectedType: Type): Boolean = actualType == expectedType

  import scala.util.matching.Regex
  val abstractionPattern: Regex = "(.*) -> (.*)".r
  def ctxToType(ctxTypeString: String): Type =
    // TODO: probably pass ctx here?? And match for type context, not expression one
    ctxTypeString match {
      case "Nat" => NatType
      case "Bool" => BoolType
      case abstractionPattern(argType, returnType) => FunctionType(ctxToType(argType), ctxToType(returnType))
      case _ => null // In fact, this one should be unsupported
    }
}
