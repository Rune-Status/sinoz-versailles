package io.versailles.domain.model

/**
  * The capacity of an inventory in slots.
  * @author Sino
  */
case class InventoryCapacity(private val value: Int) extends AnyVal {
  def toValue = value
}