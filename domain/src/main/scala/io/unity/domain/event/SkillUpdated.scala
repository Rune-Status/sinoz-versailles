package io.unity.domain.event

import io.unity.domain.model.Skill

/**
  * An event that denotes a [[Skill]] has been updated.
  * @author Sino
  */
case class SkillUpdated(
  previous: Skill,
  current: Skill
)
