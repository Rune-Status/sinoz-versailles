package io.unity.domain.model

/**
  * The name of a physical game object.
  * @author Sino
  */
case class ObjectName(private val value: String) extends AnyVal {
  def toValue = value
}
