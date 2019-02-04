package io.unity.domain.event

import io.unity.domain.model.Position

/**
  * An event that denotes a roamable map was recentered around the given
  * [[Position]].
  * @author Sino
  */
case class RoamableMapRecentered(position: Position)
