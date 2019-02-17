package io.versailles.domain.model

/**
  * A slot in an [[Inventory]] that may or may not contain an [[Item]].
  * @author Sino
  */
case class InventorySlot(id: Int, contained: Option[Item]) {
  def isEmpty = contained.isEmpty
  def nonEmpty = contained.nonEmpty

  def contain(item: Item) = copy(contained = Some(item))
  def release = copy(contained = None)

  def containedQuantity = contained.map(_.quantity).getOrElse(0)
}
