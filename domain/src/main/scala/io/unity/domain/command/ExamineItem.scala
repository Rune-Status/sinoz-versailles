package io.unity.domain.command

import io.unity.domain.model.ItemId

/**
  * A command from a player to have an item examined.
  * @author Sino
  */
case class ExamineItem(itemId: ItemId)
