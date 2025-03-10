package Stella.Error

enum StellaError(errStr: String = "Unknown error"):
  val errorText: String = errStr

  case ERROR_MISSING_MAIN extends StellaError("Main is missing")
  case ERROR_UNDEFINED_VARIABLE(varName: String) extends StellaError(s"Undefined variable: ${varName}")
  case ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected type ${expectedType} for expression ${expr}, got: ${exprType}")
  case ERROR_NOT_A_FUNCTION(expr: String, expectedType: String) extends StellaError(
    s"Expected function type ${expectedType} for ${expr}")
  case ERROR_NOT_A_TUPLE(expr: String) extends StellaError(s"${expr} expected to be a tuple")
//  case ERROR_NOT_A_RECORD
//  case ERROR_NOT_A_LIST
  case ERROR_UNEXPECTED_LAMBDA(expr: String, exprType: String, expectedType: String) extends StellaError(
  s"Expected function type ${expectedType} for expression ${expr}, got: ${exprType}")
  case ERROR_UNEXPECTED_TYPE_FOR_PARAMETER(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected type ${expectedType} for parameter ${expr}, got: ${exprType}")
  case ERROR_UNEXPECTED_TUPLE(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected tuple type ${expectedType} for ${expr}, got: ${exprType}")
  case ERROR_UNEXPECTED_RECORD(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected record type ${expectedType} for ${expr}, got: ${exprType}")
  case ERROR_UNEXPECTED_VARIANT extends StellaError()
  case ERROR_UNEXPECTED_LIST extends StellaError()
  case ERROR_UNEXPECTED_INJECTION extends StellaError()
  case ERROR_MISSING_RECORD_FIELDS extends StellaError()
  case ERROR_UNEXPECTED_RECORD_FIELDS extends StellaError()
  case ERROR_UNEXPECTED_FIELD_ACCESS extends StellaError()
  case ERROR_UNEXPECTED_VARIANT_LABEL extends StellaError()
  case ERROR_TUPLE_INDEX_OUT_OF_BOUNDS(expr: String, size: Int, index: Int) extends StellaError(
    s"Index for tuple ${expr} of size ${size} is out of bounds: ${index}")
  case ERROR_UNEXPECTED_TUPLE_LENGTH extends StellaError()
  case ERROR_AMBIGUOUS_SUM_TYPE extends StellaError()
  case ERROR_AMBIGUOUS_VARIANT_TYPE extends StellaError()
  case ERROR_AMBIGUOUS_LIST extends StellaError()
  case ERROR_ILLEGAL_EMPTY_MATCHING extends StellaError()
  case ERROR_NONEXHAUSTIVE_MATCH_PATTERNS extends StellaError()
  case ERROR_UNEXPECTED_PATTERN_FOR_TYPE extends StellaError()
  case ERROR_DUPLICATE_RECORD_FIELDS extends StellaError()
  case ERROR_DUPLICATE_RECORD_TYPE_FIELDS extends StellaError()
  case ERROR_DUPLICATE_VARIANT_TYPE_FIELDS extends StellaError()
end StellaError