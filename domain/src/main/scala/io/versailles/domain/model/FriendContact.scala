package io.versailles.domain.model

/**
  * References a user that is added to another user's friends list.
  * @author Sino
  */
case class FriendContact(
  displayName: DisplayName,
  prevDisplayName: Option[DisplayName],
  worldId: Option[WorldId]
)
