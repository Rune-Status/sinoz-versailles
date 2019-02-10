package io.versailles.domain.model

/**
  * A slot in an [[Inventory]] that may or may not contain an [[Item]].
  * @author Sino
  */
case class InventorySlot(id: Int, private val contained: Option[Item]) {
  // TODO
}
