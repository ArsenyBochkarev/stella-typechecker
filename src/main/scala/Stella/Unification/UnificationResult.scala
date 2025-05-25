package Stella.Unification

enum UnificationResult(errStr: String = ""):
  case UNIFICATION_OK extends UnificationResult
  case UNIFICATION_ERROR_INFINITE_TYPE(expr: String, left: String, right: String) extends UnificationResult
  case UNIFICATION_ERROR_FAILED(expr: String, expectedType: String, actualType: String) extends UnificationResult

