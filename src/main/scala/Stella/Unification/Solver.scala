package Stella.Unification

object Solver {
  var constraints: List[Constraint] = List[Constraint]()
  def addConstraint(c: Constraint) = c +: constraints

  def solve(): UnificationResult = ???
}
