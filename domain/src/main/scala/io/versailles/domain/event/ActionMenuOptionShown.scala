package io.versailles.domain.event

import io.versailles.domain.model.ActionMenu

/**
  * An event where an [[ActionMenu.Option]] was updated to shown.
  * @author Sino
  */
case class ActionMenuOptionShown(menuOptionType: ActionMenu.Option.Type)
