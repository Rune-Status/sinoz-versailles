package io.versailles.domain.event

import io.versailles.domain.model.Energy

/**
  * An event of a player character having their energy level drained.
  * @author Sino
  */
case class EnergyLevelDrained(
  previousLevel: Energy,
  newLevel: Energy
)
