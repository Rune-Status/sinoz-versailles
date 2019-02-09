package io.unity.domain.event

import io.unity.domain.model.Weight

/**
  * An event of a player character losing weight.
  * @author Sino
  */
case class WeightLost(
  previousWeight: Weight,
  newWeight: Weight
)
