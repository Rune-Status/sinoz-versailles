package io.unity.domain.model

/**
  * An [[Item]]'s right-click option when it is placed in a player's
  * item bag.
  * @author Sino
  */
case class ItemBagOption(private val value: String) extends AnyVal {
  def toValue = value
}