package io.unity.domain.event

import io.unity.domain.model.ActionMenu

/**
  * An event where an [[ActionMenu.Option]] was updated to shown.
  * @author Sino
  */
case class ActionMenuOptionShown(menuOptionType: ActionMenu.Option.Type)
