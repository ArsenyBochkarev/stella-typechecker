package Stella.Types

import Stella.Error.ErrorManager
import Stella.Error.StellaError.{ERROR_MISSING_RECORD_FIELDS, ERROR_UNEXPECTED_RECORD_FIELDS, ERROR_UNEXPECTED_SUBTYPE, ERROR_UNEXPECTED_TYPE_FOR_EXPRESSION}
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
        if unexpectedFields.nonEmpty && !TypeChecker.isSubtypingEnabled then
          for (label <- unexpectedFields)
            ErrorManager.registerError(ERROR_UNEXPECTED_RECORD_FIELDS(label, other.toString))
          return false
        var res = true
        if !TypeChecker.isSubtypingEnabled then
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

  override def toString: String = {
    val elements = labelsMap.map { case (label, tpe) => s"$label: $tpe" }
    s"{${elements.mkString(", ")}}"
  }
  override def hashCode: Int = labelsMap.hashCode()

  override def isSubtypeOf(other: Type): Boolean =
    //
    // ------------------------------------------------------------------ S-RcdWidth
    //   {l_1 : T_1, ..., l_n+k : T_n+k} <: {l_1 : T_1, ..., l_n : T_n}

    //                    S_1 <: T_1 ... S_n <: T_n
    // -------------------------------------------------------------- S-RcdDepth
    //   {l_1 : S_1, ..., l_n : S_n} <: {l_1 : T_1, ..., l_n : T_n}

    //   {k_1 : S_1, ..., k_n : S_n} is a permutation of {l_1 : T_1, ..., l_n : T_n}
    // ------------------------------------------------------------------------------- S-RcdPerm
    //             {k_1 : S_1, ..., k_n : S_n} <: {l_1 : T_1, ..., l_n : T_n}

    other match
      case otherRcd: RecordType =>
        // Apply S-RcdWidth first (by using otherRcd.labelsMap, cutting everything from excessive 'this')
        // Then check for S-RcdDepth (treating fields of 'this' as a permutation of otherRcd: S-RcdPerm)
        otherRcd.labelsMap.forall((otherLabel, otherTy) => {
          labelsMap.find((thisLabel, thisTy) => thisLabel == otherLabel) match
            case Some(p) => p._2.isSubtypeOf(otherTy)
            case _ => false
        })
      case TopType => true
      case _ => false
}
