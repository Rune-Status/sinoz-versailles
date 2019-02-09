package io.unity.domain.model

/**
  * The id of an interface.
  * @author Sino
  */
case class InterfaceId(private val value: Int) extends AnyVal {
  def toValue = value
}
