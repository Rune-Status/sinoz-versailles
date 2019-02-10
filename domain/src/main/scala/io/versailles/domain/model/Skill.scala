package io.versailles.domain.model

import io.versailles.domain.event.ReachedNewLevel

object Skill {
  /** The different types of skills that are supported. */
  type Type = Int

  val Attack: Type = 0
  val Defence: Type = 1
  val Strength: Type = 2
  val Hitpoints: Type = 3
  val Ranged: Type = 4
  val Prayer: Type = 5
  val Magic: Type = 6

  val CombatSkills = Attack to Magic

  val MinLevel = 1
  val MaxLevel = 99

  /** A type of skill event that is generated as a result from
    * performing a skill command. */
  type Event = Any
}

/**
  * A character skill.
  * @author Sino
  */
case class Skill(id: Skill.Type, level: Level, maxLevel: Level, experience: Experience) {
  /** Adds [[Experience]] multiplied by the given scale to this skill's current
    * [[Experience]]. It may cause a level-up. */
  def gainExperience(amount: Experience)(scale: Double): (Skill, Option[ReachedNewLevel]) = {
    val totalGain = Experience(amount.toValue * scale)
    val experienceAdded = experience + totalGain

    val currentLevel = maxLevel
    val actualLevelAfterGain = Experience.computeLevelAtExperience(experienceAdded)

    val levelDelta = actualLevelAfterGain.toValue - currentLevel.toValue
    val reachedNewLevel = levelDelta > 0
    if (!reachedNewLevel) {
      (copy(experience = experienceAdded), None)
    } else {
      val newLevel = balanceLevel(level, actualLevelAfterGain, levelDelta)
      val updatedSkill = copy(experience = experienceAdded, level = newLevel, maxLevel = actualLevelAfterGain)

      val levelUpEvent = ReachedNewLevel(updatedSkill.id, currentLevel, actualLevelAfterGain)

      (updatedSkill, Some(levelUpEvent))
    }
  }

  /** Drains the given amount from the [[Skill.level]]. This effect may
    * stack but does not exceed zero. */
  def drain(amount: Int): Skill = {
    val newLevel = Level(level.toValue - amount)
    if (newLevel < Level(0)) {
      copy(level = Level(0))
    } else {
      copy(level = newLevel)
    }
  }

  /** Raises the given amount to the [[Skill.level]]. This effect may
    * stack but does not exceed the value of 127. */
  def raise(amount: Int): Skill = {
    val newLevel = Level(level.toValue + amount)
    if (newLevel > Level(127)) {
      copy(level = Level(127))
    } else {
      copy(level = newLevel)
    }
  }

  /** Lowers the given amount from the [[Skill.level]]. This effect does
    * not stack. */
  def lower(amount: Int): Skill = {
    val newLevel = Level(level.toValue - amount)
    val allowedLevel = Level(maxLevel.toValue - amount)
    if (allowedLevel < Level(0)) {
      copy(level = Level(0))
    } else if (newLevel < allowedLevel) {
      copy(level = allowedLevel)
    } else {
      copy(level = newLevel)
    }
  }

  /** Restores the given amount to the [[Skill.level]]. This effect may
    * stack but does not exceed the [[Skill.maxLevel]]. */
  def restore(amount: Int): Skill = {
    val newLevel = Level(level.toValue + amount)
    if (newLevel > maxLevel) {
      copy(level = maxLevel)
    } else {
      copy(level = newLevel)
    }
  }

  /** Fully restores the [[Skill.level]], making it equal to the
    * [[Skill.maxLevel]].*/
  def fullyRestore(): Skill =
    copy(level = maxLevel)

  private def balanceLevel(level: Level, maxLevel: Level, amount: Int): Level =
    if (level == maxLevel) {
      maxLevel
    } else if (level < maxLevel) {
      Level(level.toValue + amount)
    } else {
      maxLevel
    }
}
