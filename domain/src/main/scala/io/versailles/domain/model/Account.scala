package io.versailles.domain.model

/**
  * A user account.
  * @author Sino
  */
case class Account(
  email: Email,
  displayName: DisplayName,
  previousDisplayName: Option[DisplayName],
  password: Password,
  rank: Option[AccountRank.Type],
  subscriptionType: AccountSubscription.Type,
  lastLogin: Option[LastLogin]
)
