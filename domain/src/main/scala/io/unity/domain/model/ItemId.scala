package io.unity.domain.model

/**
  * The id of an [[Item]].
  * @author Sino
  */
case class ItemId(private val value: Int) extends AnyVal {
  def toValue = value
}
