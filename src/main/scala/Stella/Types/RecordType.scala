package Stella.Types

import Stella.Error.ErrorManager
import Stella.Error.StellaError.{ERROR_MISSING_RECORD_FIELDS, ERROR_UNEXPECTED_RECORD_FIELDS, ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION}
import Stella.TypeChecker

class RecordType(recordStructure: List[(String, Type)]) extends Type {
  val labelsMap: List[(String, Type)] = recordStructure

  override def equals(other: Any): Boolean =
    other match
      case that: RecordType =>
        // Check only fields labels first
        val currentFieldsLabels = labelsMap.map((fst, snd) => fst)
        val otherFieldsLabels = that.labelsMap.map((fst, snd) => fst)
        val missingFields = otherFieldsLabels.diff(currentFieldsLabels)
        val unexpectedFields = currentFieldsLabels.diff(otherFieldsLabels)
        if missingFields.nonEmpty then
          for (label <- missingFields)
            ErrorManager.registerError(ERROR_MISSING_RECORD_FIELDS(label, other.toString))
          return false
        if unexpectedFields.nonEmpty then
          for (label <- unexpectedFields)
            ErrorManager.registerError(ERROR_UNEXPECTED_RECORD_FIELDS(label, other.toString))
          return false

        // Type check for fields next
        var res = true
        for ((currentField, otherField) <- labelsMap zip that.labelsMap)
          if !TypeChecker.validate(currentField._2, otherField._2, s"$toString == ${that.toString}") then
            ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
              currentField._1, currentField._2.toString, otherField._2.toString))
            res = false
        res
      case _ => false
  override def toString: String = {
    val elements = labelsMap.map { case (label, tpe) => s"$label: $tpe" }
    s"{${elements.mkString(", ")}}"
  }
  override def hashCode: Int = labelsMap.hashCode()
}
