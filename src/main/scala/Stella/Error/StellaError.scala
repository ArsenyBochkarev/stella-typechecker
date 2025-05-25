package Stella.Error

enum StellaError(errStr: String = "Unknown error"):
  val errorText: String = errStr

  case ERROR_MISSING_MAIN() extends StellaError("Function \"main\" is missing")
  case ERROR_UNDEFINED_VARIABLE(varName: String) extends StellaError(s"Undefined variable: ${varName}")
  case ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected type ${expectedType} for expression ${expr}, got: ${exprType}")
  case ERROR_NOT_A_FUNCTION(expr: String, exprType: String) extends StellaError(
    s"Expected function type for ${expr}, got: $exprType")
  case ERROR_NOT_A_TUPLE(expr: String) extends StellaError(s"${expr} expected to be a tuple")
  case ERROR_NOT_A_RECORD(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected record type $expectedType for $expr, got: $exprType")
  case ERROR_NOT_A_LIST(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected list type $expectedType for $expr, got: $exprType")
  case ERROR_UNEXPECTED_LAMBDA(expr: String, exprType: String, expectedType: String) extends StellaError(
  s"Expected function type ${expectedType} for expression \"${expr}\", got: ${exprType}")
  case ERROR_UNEXPECTED_TYPE_FOR_PARAMETER(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected type ${expectedType} for parameter \"${expr}\", got: ${exprType}")
  case ERROR_UNEXPECTED_TUPLE(expr: String, exprType: String, expectedType: String) extends StellaError(
    s"Expected tuple type ${expectedType} for ${expr}, got: ${exprType}")
  case ERROR_UNEXPECTED_RECORD(expectedType: String, actualType: String) extends StellaError(
    s"Expected non-record type ${expectedType}, got: ${actualType}")
  case ERROR_UNEXPECTED_VARIANT(expectedType: String, actualType: String) extends StellaError(
    s"Expected non-variant type $expectedType, got: $actualType")
  case ERROR_UNEXPECTED_LIST(expectedType: String, actualType: String) extends StellaError(
    s"Expected non-list type $expectedType, got: $actualType")
  case ERROR_UNEXPECTED_INJECTION(expr: String, actualType: String) extends StellaError(
    s"Unexpected injection for $expr of type $actualType")
  case ERROR_MISSING_RECORD_FIELDS(field: String, rec: String) extends StellaError(
    s"Missing field: \"$field\" for record $rec")
  case ERROR_UNEXPECTED_RECORD_FIELDS(field: String, rec: String) extends StellaError(
    s"Unexpected field: \"$field\" for record $rec")
  case ERROR_UNEXPECTED_FIELD_ACCESS(field: String, rec: String) extends StellaError(
    s"Unexpected field \"$field\" access for record $rec")
  case ERROR_UNEXPECTED_VARIANT_LABEL(label: String, variant: String) extends StellaError(
    s"Unexpected label \"$label\" for variant $variant")
  case ERROR_TUPLE_INDEX_OUT_OF_BOUNDS(expr: String, size: Int, index: Int) extends StellaError(
    s"Index for tuple ${expr} of size ${size} is out of bounds: ${index}")
  case ERROR_UNEXPECTED_TUPLE_LENGTH(tup: String, expectedNumber: Int, actualNumber: Int) extends StellaError(
    s"Expected $expectedNumber elements in a tuple $tup, got $actualNumber")
  case ERROR_AMBIGUOUS_SUM_TYPE(expr: String) extends StellaError(
    s"Unable to determine injection type for $expr")
  case ERROR_AMBIGUOUS_VARIANT_TYPE(expr: String) extends StellaError(
    s"Unable to determine variant type for $expr")
  case ERROR_AMBIGUOUS_LIST(expr: String) extends StellaError(
    s"Unable to determine list type for $expr")
  case ERROR_ILLEGAL_EMPTY_MATCHING(expr: String) extends StellaError (
    s"Match expression $expr with an empty list")
  case ERROR_NONEXHAUSTIVE_MATCH_PATTERNS(expr: String) extends StellaError(
    s"Non-exhaustive pattern matching for $expr")
  case ERROR_UNEXPECTED_PATTERN_FOR_TYPE(varType: String, pat: String) extends StellaError(
    s"Unexpected pattern \"$pat\" for type $varType")
  case ERROR_DUPLICATE_RECORD_FIELDS(field: String, rec: String) extends StellaError(
    s"Duplicate record field \"$field\" in a record $rec")
  case ERROR_DUPLICATE_RECORD_TYPE_FIELDS(field: String, rec: String) extends StellaError(
    s"Duplicate record field \"$field\" in a record type $rec")
  case ERROR_DUPLICATE_VARIANT_TYPE_FIELDS(field: String, variant: String) extends StellaError(
    s"Duplicate variant field \"$field\" in a field type $variant")
  case ERROR_OCCURS_CHECK_INFINITE_TYPE(expr: String) extends StellaError(
    s"Infinite type occured in expression $expr")
  case ERROR_NOT_A_GENERIC_FUNCTION(expr: String, expectedType: String) extends StellaError(
    s"Expected generic function type $expectedType for $expr")
  case ERROR_INCORRECT_NUMBER_OF_TYPE_ARGUMENTS(expr: String, expectedNumber: Int, actualNumber: Int) extends StellaError(
    s"Expected $expectedNumber arguments for type instantiation of $expr, got $actualNumber")
  case ERROR_UNDEFINED_TYPE_VARIABLE(variable: String) extends StellaError(
    s"Undefined type variable $variable")
  case ERROR_AMBIGUOUS_REFERENCE_TYPE(expr: String) extends StellaError(
    s"Unable to determine memory type for $expr")
  case ERROR_UNEXPECTED_MEMORY_ADDRESS(expr: String, expectedType: String) extends StellaError(
    s"Unexpected reference \"$expr\" for type $expectedType")
  case ERROR_NOT_A_REFERENCE(expr: String) extends StellaError(
    s"Expected reference type for $expr")
  case ERROR_EXCEPTION_TYPE_NOT_DECLARED() extends StellaError(
    "Exception type is not declared")
  case ERROR_AMBIGUOUS_THROW_TYPE(expr: String) extends StellaError(
    s"Unable to determine exception type for $expr")
end StellaError