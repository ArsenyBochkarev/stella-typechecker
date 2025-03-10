package Stella.Types

class RecordType(recordStructure: List[(String, Type)]) extends Type {
  val labelsMap: List[(String, Type)] = recordStructure

  override def equals(other: Any): Boolean =
    val missingFields = ???
    val extraFields = ???
    other match
      case that: RecordType => labelsMap == that.labelsMap
      case _ => false
  override def toString: String = {
    val elements = labelsMap.map { case (label, tpe) => s"$label: $tpe" }
    s"{${elements.mkString(", ")}}"
  }
  override def hashCode: Int = labelsMap.hashCode()
}
