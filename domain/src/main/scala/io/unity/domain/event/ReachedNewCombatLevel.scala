package io.unity.domain.event

import io.unity.domain.model.CombatLevel

/**
  * An event that denotes that a player has reached a new [[CombatLevel]].
  * @author Sino
  */
case class ReachedNewCombatLevel(
  previous: CombatLevel,
  current: CombatLevel
)
