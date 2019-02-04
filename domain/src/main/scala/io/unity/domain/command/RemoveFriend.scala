package io.unity.domain.command

import io.unity.domain.model.DisplayName

/**
  * A command to remove a user from a player's friends list.
  * @author Sino
  */
case class RemoveFriend(displayName: DisplayName)
