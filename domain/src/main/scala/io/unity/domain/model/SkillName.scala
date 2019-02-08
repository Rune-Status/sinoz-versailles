package io.unity.domain.model

/**
  * The name of a [[Skill]].
  * @author Sino
  */
case class SkillName(private val value: String) extends AnyVal {
  def toValue = value
}