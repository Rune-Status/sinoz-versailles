package io.versailles.application.model

import io.versailles.domain.model.Position

/**
  * A client's reconnection with a game world.
  * @author Sino
  */
case class Reconnection(
  position: Position.TileScope,
  playerLocations: Seq[Int]
)