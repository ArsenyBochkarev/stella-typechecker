package Stella.Unification

import Stella.Types.Type

class Constraint(l: Type, r: Type, e: String) {
  val left: Type = l
  val right: Type = r
  val expr: String = e
}
