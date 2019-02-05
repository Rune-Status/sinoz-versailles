package io.unity.domain.model

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
}

/**
  * A character skill.
  * @author Sino
  */
case class Skill(id: Skill.Type, level: Level, maxLevel: Level, experience: Experience) {
  // TODO
}
