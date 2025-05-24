package Stella.Unification

import Stella.Types.Type

class Constraint(l: Type, r: Type, e: String) {
  val left: Type = l
  val right: Type = r
  val expr: String = e

  override def toString() = s"${l.toString} = ${r.toString}"

  // This does [lt |-> rt] in this constraint
  def replace(lt: Type, rt: Type): Constraint =
    Constraint(left.replace(lt, rt), right.replace(lt, rt), expr)
}
