package io.unity.domain.model

object CombatBonus {
  object Aggressive {
    case object Stab extends Type
    case object Slash extends Type
    case object Crush extends Type
    case object Magic extends Type
    case object Ranged extends Type

    sealed abstract class Type
  }

  object Defensive {
    case object Stab extends Type
    case object Slash extends Type
    case object Crush extends Type
    case object Magic extends Type
    case object Ranged extends Type

    sealed abstract class Type
  }

  object Other {
    case object MonsterMeleeAttack extends Type
    case object MonsterMeleeStrength extends Type

    case object MonsterRangedStrength extends Type
    case object MonsterMagicStrength extends Type

    sealed abstract class Type
  }
}

/**
  * A combat bonus.
  * @author Sino
  */
case class CombatBonus(private val value: Int) extends AnyVal {
  def toValue = value
}
