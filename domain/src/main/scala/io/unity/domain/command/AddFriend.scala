package io.unity.domain.command

import io.unity.domain.model.DisplayName

/**
  * A command to add a user to a player's friends list.
  * @author Sino
  */
case class AddFriend(displayName: DisplayName)