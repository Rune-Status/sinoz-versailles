package io.versailles.domain.command

import io.versailles.domain.model.Movement

/**
  * A command to move from one tile to another.
  * @author Sino
  */
case class MoveToTile(
  x: Int,
  y: Int,
  insertedFlagOnMinimap: Boolean,
  cheat: Option[Movement.Cheat.Type]
)
