package io.unity.application.model

/**
  * A 32-bit seed of a 128-bit key.
  * @author Sino
  */
case class Seed(private val value: Int) extends AnyVal {
  def isValid = value != 0

  def toValue = value
}
