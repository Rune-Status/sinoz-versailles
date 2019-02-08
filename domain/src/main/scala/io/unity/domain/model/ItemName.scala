package io.unity.domain.model

/**
  * The name of an [[Item]].
  * @author Sino
  */
case class ItemName(private val value: String) extends AnyVal {
  def toValue = value
}
