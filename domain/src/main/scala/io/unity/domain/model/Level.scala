package io.unity.domain.model

/** A skill level.
  * @author Sino
  */
case class Level(private val value: Int) extends AnyVal {
  def <(other: Level) = value < other.value
  def <=(other: Level) = value <= other.value

  def >(other: Level) = value > other.value
  def >=(other: Level) = value >= other.value

  def toValue = value
}
