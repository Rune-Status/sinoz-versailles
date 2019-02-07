package io.unity.application.model

import io.unity.domain.model.Position

/**
  * A client's reconnection with a game world.
  * @author Sino
  */
case class Reconnection(
  position: Position,
  playerLocations: Seq[Int]
)