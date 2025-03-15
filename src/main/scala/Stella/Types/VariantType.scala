package Stella.Types

import Stella.Error.ErrorManager
import Stella.Error.StellaError.{ERROR_UNEXPECTED_VARIANT_LABEL, ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION}
import Stella.TypeChecker

class VariantType(variantStructure: List[(String, Type)]) extends Type {
  val labelsMap: List[(String, Type)] = variantStructure

  override def equals(other: Any): Boolean =
    other match {
      case that: VariantType =>
        // Check labels first
        val currentFieldsLabels = labelsMap.map((fst, snd) => fst)
        val otherFieldsLabels = that.labelsMap.map((fst, snd) => fst)
        val unexpectedFields = currentFieldsLabels.diff(otherFieldsLabels)
        if unexpectedFields.nonEmpty then
          for (label <- unexpectedFields)
            ErrorManager.registerError(ERROR_UNEXPECTED_VARIANT_LABEL(label, other.toString))
          return false

        // Type check for fields next
        var res = true
        for ((currentField, otherField) <- labelsMap zip that.labelsMap)
          if !TypeChecker.validate(currentField._2, otherField._2) then
            ErrorManager.registerError(ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION(
              currentField._1, currentField._2.toString, otherField._2.toString))
            res = false
        res
      case _ => false
    }

  override def toString: String = {
    val elements = labelsMap.map { case (label, tpe) => s"$label: $tpe" }
    s"<|${elements.mkString(", ")}|>"
  }
  override def hashCode: Int = labelsMap.hashCode()
}
