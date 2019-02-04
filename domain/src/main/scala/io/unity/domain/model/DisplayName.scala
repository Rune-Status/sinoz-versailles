package io.unity.domain.model

/**
  * The name to display of a player's character.
  * @author Sino
  */
case class DisplayName(private val value: String) extends AnyVal {
  def isValid = value.length >= 3 && value.length() <= 15

  def toValue = value
}
