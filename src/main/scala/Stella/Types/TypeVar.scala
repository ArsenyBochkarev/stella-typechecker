package Stella.Types

class TypeVar(i: Int) extends Type {
  val index: Int = i

  override def equals(other: Any): Boolean =
    other match
      case that: TypeVar => index == that.index
      case _ => false
  override def toString: String = s"T_$index"
  override def hashCode: Int = index.hashCode()
}

// Companion object to track number of TypeVar's
object TypeVarWrapper:
  private var counter = 0
  def createTypeVar(): Type = { counter += 1; TypeVar(counter) }
