package Stella.Types

import Stella.Error.ErrorManager
import Stella.Error.StellaError.{ERROR_UNEXPECTED_SUBTYPE, ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION, ERROR_UNEXPECTED_VARIANT_LABEL}
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
        var res = true
        if !TypeChecker.isSubtypingEnabled && !TypeChecker.isSubtypingEnabled then
          for ((currentField, otherField) <- labelsMap zip that.labelsMap)
            if currentField._1 != otherField._1 then
              res = false
        if !res then return res

        // Type check for fields next
        if TypeChecker.isSubtypingEnabled then
          for ((thisLabel, thisTy) <- labelsMap)
            val otherTy = that.labelsMap.find((otherLabel, otherTy) => thisLabel == otherLabel) match {
              case Some(p) => p._2
              case _ => null
            }
            if otherTy == null then res = false
            else
              if !TypeChecker.validate(thisLabel, thisTy, otherTy) then
                res = false
        else
          for ((currentField, otherField) <- labelsMap zip that.labelsMap)
            if !TypeChecker.validate(currentField._1, currentField._2, otherField._2) then
              res = false
        res
      case _ => false
    }

  override def toString: String = {
    val elements = labelsMap.map { case (label, tpe) => s"$label: $tpe" }
    s"<|${elements.mkString(", ")}|>"
  }
  override def hashCode: Int = labelsMap.hashCode()

  override def isSubtypeOf(other: Type): Boolean =
    //
    // ------------------------------------------------------------------ S-VariantWidth
    //   <l_1 : T_1, ..., l_n : T_n> <: <l_1 : T_1, ..., l_n+k : T_n+k>

    //                    S_1 <: T_1 ... S_n <: T_n
    // -------------------------------------------------------------- S-VariantDepth
    //   <l_1 : S_1, ..., l_n : S_n> <: <l_1 : T_1, ..., l_n : T_n>

    //   <k_1 : S_1, ..., k_n : S_n> is a permutation of <l_1 : T_1, ..., l_n : T_n>
    // ------------------------------------------------------------------------------- S-VariantPerm
    //             <k_1 : S_1, ..., k_n : S_n> <: <l_1 : T_1, ..., l_n : T_n>

    other match
      case otherVariant: VariantType =>
        // Apply S-VariantWidth first (by using this.labelsMap, cutting everything from excessive otherVariant)
        // Then check for S-VariantDepth (treating fields of 'this' as a permutation of otherVariant: S-VariantPerm)
        labelsMap.forall((thisLabel, thisTy) => {
          thisTy.isSubtypeOf(otherVariant.labelsMap.find((otherLabel, otherTy) => thisLabel == otherLabel).get._2)
        })
      case TopType => true
      case _ => false
}
