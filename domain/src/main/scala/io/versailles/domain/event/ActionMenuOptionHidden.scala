package io.versailles.domain.event

import io.versailles.domain.model.ActionMenu

/**
  * An event where an [[ActionMenu.Option]] was updated to hidden.
  * @author Sino
  */
case class ActionMenuOptionHidden(menuOptionType: ActionMenu.Option.Type)
