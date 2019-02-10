package io.versailles.domain.event

import io.versailles.domain.model.{Level, Skill}

/**
  * An event that denotes a [[Skill]] has reached a new [[Level]].
  * @author Sino
  */
case class ReachedNewLevel(
  skillType: Skill.Type,
  previous: Level,
  current: Level
)
