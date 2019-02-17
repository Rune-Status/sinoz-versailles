package io.versailles.domain.model

/**
  * A player character's game state profile.
  * @author Sino
  */
case class CharacterProfile(
  position: Position.TileScope,
  ironMan: Option[IronMan.Type]
)
