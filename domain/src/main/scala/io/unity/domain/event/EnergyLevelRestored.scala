package io.unity.domain.event

import io.unity.domain.model.Energy

/**
  * An event of a player character having their energy level restored.
  * @author Sino
  */
case class EnergyLevelRestored(
  previousLevel: Energy,
  newLevel: Energy
)
