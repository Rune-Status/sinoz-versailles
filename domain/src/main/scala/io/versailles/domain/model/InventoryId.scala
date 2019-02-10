package io.versailles.domain.model

/**
  * The id of an inventory of [[Item]]s.
  * @author Sino
  */
case class InventoryId(private val value: Int) extends AnyVal {
  def toValue = value
}
