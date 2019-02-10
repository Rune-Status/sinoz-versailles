package io.versailles.domain.model

/**
  * Describes an item.
  * @author Sino
  */
case class ItemDescriptor(
  id: ItemId,
  name: ItemName,
  examine: ItemTextDescription,

  bagOptions: Vector[ItemBagOption],
  floorOptions: Vector[ItemFloorOption],

  subscriptionType: AccountSubscription.Type,
  bankPlaceholderId: Option[ItemPlaceholderId],

  stackable: Boolean,
)
