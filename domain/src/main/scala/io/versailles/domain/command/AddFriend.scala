package io.versailles.domain.command

import io.versailles.domain.model.DisplayName

/**
  * A command to add a user to a player's friends list.
  * @author Sino
  */
case class AddFriend(displayName: DisplayName)