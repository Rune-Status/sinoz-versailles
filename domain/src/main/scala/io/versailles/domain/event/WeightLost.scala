package io.versailles.domain.event

import io.versailles.domain.model.Weight

/**
  * An event of a player character losing weight.
  * @author Sino
  */
case class WeightLost(
  previousWeight: Weight,
  newWeight: Weight
)
