package io.versailles.domain.command

import io.versailles.domain.model.DisplayName

/**
  * A command to remove a user from a player's ignore list.
  * @author Sino
  */
case class RemoveIgnored(displayName: DisplayName)