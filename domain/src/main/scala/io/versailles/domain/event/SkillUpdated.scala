package io.versailles.domain.event

import io.versailles.domain.model.Skill

/**
  * An event that denotes a [[Skill]] has been updated.
  * @author Sino
  */
case class SkillUpdated(
  previous: Skill,
  current: Skill
)
