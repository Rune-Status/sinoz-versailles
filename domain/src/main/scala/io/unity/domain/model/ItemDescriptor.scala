package io.unity.domain.model

/**
  * Describes an item.
  * @author Sino
  */
case class ItemDescriptor(
  id: ItemId,
  name: ItemName,
  examine: ItemTextDescription
)
