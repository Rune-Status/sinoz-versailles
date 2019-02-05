package io.unity.domain.model

import io.unity.domain.event.{ReachedNewCombatLevel, SkillUpdated}

/**
  * A set of [[Skill]]s.
  * @author Sino
  */
case class SkillSet(private val skills: Vector[Skill]) {
  /** Adds [[Experience]] to the specified [[Skill]]. */
  def gainExperience(id: Skill.Type, gain: Experience)(scale: Double): (SkillSet, Seq[Skill.Event]) = {
    val targetSkill = skills(id)

    val (newSkill, levelUpEvent) = targetSkill.gainExperience(gain)(scale)
    val newSkillSet = copy(skills = skills.updated(id, newSkill))

    val skillUpdateEvent = SkillUpdated(targetSkill, newSkill)
    if (levelUpEvent.isEmpty) {
      (newSkillSet, Seq(skillUpdateEvent))
    } else {
      val curCbLvl = CombatLevel.evaluate(skills)
      val newCbLvl = CombatLevel.evaluate(newSkillSet.skills)

      val reachedNewCbLvl = newCbLvl > curCbLvl
      if (reachedNewCbLvl) {
        val cbUpdateEvent = ReachedNewCombatLevel(curCbLvl, newCbLvl)

        (newSkillSet, Seq(skillUpdateEvent, cbUpdateEvent, levelUpEvent.get))
      } else {
        (newSkillSet, Seq(skillUpdateEvent, levelUpEvent.get))
      }
    }
  }

  /** Drains the [[Level]] of the specified [[Skill]]. */
  def drain(id: Skill.Type, amount: Int): (SkillSet, SkillUpdated) = {
    val targetSkill = skills(id)

    val newSkill = targetSkill.drain(amount)
    val newSkillSet = copy(skills = skills.updated(id, newSkill))

    (newSkillSet, SkillUpdated(targetSkill, newSkill))
  }

  /** Raises the [[Level]] of the specified [[Skill]]. */
  def raise(id: Skill.Type, amount: Int): (SkillSet, SkillUpdated) = {
    val targetSkill = skills(id)

    val newSkill = targetSkill.raise(amount)
    val newSkillSet = copy(skills = skills.updated(id, newSkill))

    (newSkillSet, SkillUpdated(targetSkill, newSkill))
  }

  /** Lowers the [[Level]] of the specified [[Skill]]. */
  def lower(id: Skill.Type, amount: Int): (SkillSet, SkillUpdated) = {
    val targetSkill = skills(id)

    val newSkill = targetSkill.lower(amount)
    val newSkillSet = copy(skills = skills.updated(id, newSkill))

    (newSkillSet, SkillUpdated(targetSkill, newSkill))
  }

  /** Partially restores the [[Level]] of the specified [[Skill]]. */
  def restore(id: Skill.Type, amount: Int): (SkillSet, SkillUpdated) = {
    val targetSkill = skills(id)

    val newSkill = targetSkill.restore(amount)
    val newSkillSet = copy(skills = skills.updated(id, newSkill))

    (newSkillSet, SkillUpdated(targetSkill, newSkill))
  }

  /** Fully restores the [[Level]] of the specified [[Skill]]. */
  def fullyRestore(id: Skill.Type): (SkillSet, SkillUpdated) = {
    val targetSkill = skills(id)

    val newSkill = targetSkill.fullyRestore()
    val newSkillSet = copy(skills = skills.updated(id, newSkill))

    (newSkillSet, SkillUpdated(targetSkill, newSkill))
  }

  def combatLevel = CombatLevel.evaluate(skills)
  def totalLevel = TotalLevel.evaluate(skills)
}
