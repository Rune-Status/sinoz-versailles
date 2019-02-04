package io.unity.domain.model

/**
  * A player character's game state profile.
  * @author Sino
  */
case class CharacterProfile(
  ironMan: Option[IronMan.Type]
)
