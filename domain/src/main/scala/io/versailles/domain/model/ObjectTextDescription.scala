package io.versailles.domain.model

/**
  * The textual description of a physical game object. This is used for
  * scenarios such as examining an object.
  * @author Sino
  */
case class ObjectTextDescription(private val value: String) extends AnyVal {
  def toValue = value
}
