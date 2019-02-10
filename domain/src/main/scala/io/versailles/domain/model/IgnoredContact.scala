package io.versailles.domain.model

/**
  * References a user that is added to another user's ignore list.
  * @author Sino
  */
case class IgnoredContact(
  displayName: DisplayName,
  prevDisplayName: Option[DisplayName]
)