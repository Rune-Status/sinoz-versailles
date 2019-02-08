package io.unity.domain.model

/**
  * An [[Item]]'s right-click option when it is placed on the floor
  * on the game map.
  * @author Sino
  */
case class ItemFloorOption(private val value: String) extends AnyVal {
  def toValue = value
}