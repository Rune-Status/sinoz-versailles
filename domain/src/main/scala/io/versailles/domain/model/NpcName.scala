package io.versailles.domain.model

/**
  * The name of a non-player-character.
  * @author Sino
  */
case class NpcName(private val value: String) extends AnyVal {
  def toValue = value
}
