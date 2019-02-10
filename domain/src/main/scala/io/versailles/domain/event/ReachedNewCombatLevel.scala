package io.versailles.domain.event

import io.versailles.domain.model.CombatLevel

/**
  * An event that denotes that a player has reached a new [[CombatLevel]].
  * @author Sino
  */
case class ReachedNewCombatLevel(
  previous: CombatLevel,
  current: CombatLevel
)
