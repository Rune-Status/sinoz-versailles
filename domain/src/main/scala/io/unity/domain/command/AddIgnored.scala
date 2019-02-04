package io.unity.domain.command

import io.unity.domain.model.DisplayName

/**
  * A command to add a user to a player's ignore list.
  * @author Sino
  */
case class AddIgnored(displayName: DisplayName)
