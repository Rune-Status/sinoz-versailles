package io.unity.domain.model

object CombatLevel {
  /** Evaluates the given [[Vector]] of [[Skill]]s to compute a
    * new [[CombatLevel]]. */
  def evaluate(set: Vector[Skill]) = {
    val attack = set(Skill.Attack).maxLevel.toValue
    val strength = set(Skill.Strength).maxLevel.toValue
    val defence = set(Skill.Defence).maxLevel.toValue
    val hitpoints = set(Skill.Hitpoints).maxLevel.toValue
    val prayer = set(Skill.Prayer).maxLevel.toValue
    val ranged = set(Skill.Ranged).maxLevel.toValue
    val magic = set(Skill.Magic).maxLevel.toValue

    var combat = (((defence + hitpoints + prayer / 2) * 0.25) + 1).toInt

    val melee: Double = (attack + strength) * 0.325
    val ranger: Double = Math.floor(ranged * 1.5) * 0.325
    val mager: Double = Math.floor(magic * 1.5) * 0.325

    if (melee >= ranger && melee >= mager) {
      combat += melee.toInt
    } else if (ranger >= melee && ranger >= mager) {
      combat += ranger.toInt
    } else if (mager >= melee && mager >= ranger) {
      combat += mager.toInt
    }

    CombatLevel(Level(combat))
  }
}

/**
  * A combat level is a calculation of all combat skills combined.
  * @author Sino
  */
case class CombatLevel(private val level: Level) {
  def reevaluate(set: Vector[Skill]) = CombatLevel.evaluate(set)

  def >(other: CombatLevel) = toValue > other.toValue
  def >=(other: CombatLevel) = toValue >= other.toValue

  def <(other: CombatLevel) = toValue < other.toValue
  def <=(other: CombatLevel) = toValue <= other.toValue

  def toValue = level.toValue
}
