package io.unity.domain.model

/**
  * The text content of a user interface label component.
  * @author Sino
  */
case class UILabelText(private val value: String) extends AnyVal {
  def toValue = value
}
