package Stella.Types

object BotType extends Type {
  override def toString: String = "Bot"

  override def isSubtypeOf(other: Type): Boolean = true
}
