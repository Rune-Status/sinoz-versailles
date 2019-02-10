package io.versailles.domain.command

import io.versailles.domain.model.DisplayName

/**
  * A command to remove a user from a player's friends list.
  * @author Sino
  */
case class RemoveFriend(displayName: DisplayName)
