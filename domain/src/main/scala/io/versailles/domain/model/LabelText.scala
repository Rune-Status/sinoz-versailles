package io.versailles.domain.model

/**
  * The text content of a user interface label component.
  * @author Sino
  */
case class LabelText(private val value: String) extends AnyVal {
  def toValue = value
}
