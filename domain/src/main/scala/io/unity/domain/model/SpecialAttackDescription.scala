package io.unity.domain.model

/**
  * The description of a special combat attack.
  * @author Sino
  */
case class SpecialAttackDescription(private val value: String) extends AnyVal {
  def toValue = value
}