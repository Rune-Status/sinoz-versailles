package io.versailles.domain.event

import io.versailles.domain.model.{Account, CharacterProfile, PID}

/**
  * An event of a user having logged in a game world.
  * @author Sino
  */
case class UserLoggedIn(
  account: Account,
  characterProfile: CharacterProfile,
  pid: PID
)
