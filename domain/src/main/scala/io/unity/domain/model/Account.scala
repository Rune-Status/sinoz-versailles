package io.unity.domain.model

/**
  * A user account.
  * @author Sino
  */
case class Account(
  email: Email,
  displayName: DisplayName,
  previousDisplayName: Option[DisplayName],
  password: Password,
  subscriptionType: AccountSubscription.Type,
  lastLogin: Option[LastLogin]
)
