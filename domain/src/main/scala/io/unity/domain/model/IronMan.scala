package io.unity.domain.model

/**
  * The game mode of a player's character.
  * @author Sino
  */
object IronMan {
  case object Regular extends Type
  case object Ultimate extends Type
  case object Hardcore extends Type
  sealed abstract class Type
}
