package Stella.Unification

import Stella.Unification.UnificationResult.{UNIFICATION_ERROR_FAILED, UNIFICATION_OK}
import Stella.Types.*

object Solver {
  var constraints: Set[Constraint] = Set[Constraint]()
  def addConstraint(c: Constraint) = constraints = constraints + c

  def solve(): UnificationResult = {
    // if C == emptySet then []
    if constraints.isEmpty then
      UNIFICATION_OK
    else // Let {S = T}
      val constraintsList = constraints.toList
      val currentConstraint = constraintsList.head
      val left = currentConstraint.left
      val right = currentConstraint.right
      val expr = currentConstraint.expr
      constraints = constraints.drop(1)

      // S == T
      if left == right then
        return solve()
      left match
        // if X == T and X not in FV(T)
        case l: TypeVar =>
          if !isFV(l, right) then
            constraints = constraints.toList.map(c => { c.replace(l, right) }).toSet
            return solve()
          else
            return UNIFICATION_ERROR_FAILED(expr, right.toString, l.toString)
        // if S = S_1 -> S_2 and T = T_1 -> T_2
        case l: FunctionType =>
          right match {
            case r: FunctionType =>
              addConstraint(Constraint(l.argType, r.argType, expr))
              addConstraint(Constraint(l.returnType, r.returnType, expr))
              return solve()
            case _ =>
          }
        // if S = S_1 + S_2 and T = T_1 + T_2
        case l: SumType =>
          right match {
            case r: SumType =>
              addConstraint(Constraint(l.typePair._1, r.typePair._1, expr))
              addConstraint(Constraint(l.typePair._2, r.typePair._2, expr))
              return solve()
            case _ =>
          }
        case l: ListType =>
          right match {
            case r: ListType =>
              addConstraint(Constraint(l.listType, r.listType, expr))
              return solve()
            case _ =>
          }
        case l: TupleType =>
          right match
            case r: TupleType =>
              l.elementsTypes.zip(r.elementsTypes).map(
                (t1, t2) => { addConstraint(Constraint(t1, t2, expr)); (t1, t2) }
              )
              return solve()

        case _ =>

      // if T == X and X not in FV(S)
      right match
        case r: TypeVar =>
          if !isFV(r, left) then
            constraints.toList.map(c => { c.replace(r, left) })
            solve()
          else
            UNIFICATION_ERROR_FAILED(expr, left.toString, r.toString)
        case _ => UNIFICATION_ERROR_FAILED(expr, right.toString, left.toString)
  }

  def isFV(left: Type, right: Type): Boolean = {
    right match {
      case r: TypeVar =>
        left == right
      case r: ListType =>
        isFV(left, r.listType)
      case r: FunctionType =>
        isFV(left, r.argType) || isFV(left, r.returnType)
      case r: SumType =>
        isFV(left, r.typePair._1) || isFV(left, r.typePair._2)
      case r: TupleType =>
        r.elementsTypes.exists(elem => { isFV(left, elem) })

      case _ =>
        false
    }
  }
}
