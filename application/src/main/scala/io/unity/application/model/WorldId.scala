package io.unity.application.model

/**
  * The id of a running game world.
  * @author Sino
  */
case class WorldId(private val value: Int) extends AnyVal {
  def toValue = value
}
