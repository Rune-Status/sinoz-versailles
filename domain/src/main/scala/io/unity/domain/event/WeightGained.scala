package io.unity.domain.event

import io.unity.domain.model.Weight

/**
  * An event of a player character gaining weight.
  * @author Sino
  */
case class WeightGained(
  previousWeight: Weight,
  newWeight: Weight
)
