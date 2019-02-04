package io.unity.domain.command

import io.unity.domain.model.Movement

/**
  * A client's command to move to a nearby tile.
  * @author Sino
  */
case class MoveToTile(
  x: Int,
  y: Int,
  insertedFlagOnMinimap: Boolean,
  cheat: Option[Movement.Cheat.Type]
)
