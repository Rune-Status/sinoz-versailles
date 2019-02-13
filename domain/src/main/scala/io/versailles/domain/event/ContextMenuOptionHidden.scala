package io.versailles.domain.event

import io.versailles.domain.model.ContextMenu

/**
  * An event where an [[ContextMenu.Option]] was updated to hidden.
  * @author Sino
  */
case class ContextMenuOptionHidden(menuOptionType: ContextMenu.Option.Type)
