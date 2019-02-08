package io.unity.domain.model

/**
  * The id of a physical game object.
  * @author Sino
  */
case class ObjectId(private val value: Int) extends AnyVal {
  def toValue = value
}
