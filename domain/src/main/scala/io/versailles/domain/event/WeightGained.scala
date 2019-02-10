package io.versailles.domain.event

import io.versailles.domain.model.Weight

/**
  * An event of a player character gaining weight.
  * @author Sino
  */
case class WeightGained(
  previousWeight: Weight,
  newWeight: Weight
)
