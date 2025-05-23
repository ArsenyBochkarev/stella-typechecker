package Stella.Unification

object Solver {
  var constraints: Set[Constraint] = Set[Constraint]()
  def addConstraint(c: Constraint) = constraints = constraints + c

  def solve(): UnificationResult = ???
}
