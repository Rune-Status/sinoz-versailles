package io.versailles.domain.command

import io.versailles.domain.model.ItemId

/**
  * A command from a player to have an item examined.
  * @author Sino
  */
case class ExamineItem(itemId: ItemId)
