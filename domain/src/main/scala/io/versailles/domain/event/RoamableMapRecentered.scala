package io.versailles.domain.event

import io.versailles.domain.model.Position

/**
  * An event that denotes a roamable map was recentered around the given
  * [[Position]].
  * @author Sino
  */
case class RoamableMapRecentered(position: Position)
