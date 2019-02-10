package io.versailles.domain.model

/**
  * The textual description of an [[Item]]. This is used for scenarios such as
  * examining an [[Item]].
  * @author Sino
  */
case class ItemTextDescription(private val value: String) extends AnyVal {
  def toValue = value
}
