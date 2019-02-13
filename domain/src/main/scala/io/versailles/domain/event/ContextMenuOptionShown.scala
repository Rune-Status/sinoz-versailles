package io.versailles.domain.event

import io.versailles.domain.model.ContextMenu

/**
  * An event where an [[ContextMenu.Option]] was updated to shown.
  * @author Sino
  */
case class ContextMenuOptionShown(menuOptionType: ContextMenu.Option.Type)
