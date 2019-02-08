package io.unity.domain.model

/**
  * The type of account subscription.
  * @author Sino
  */
object AccountSubscription {
  case object Free extends Type
  case object Paid extends Type
  sealed abstract class Type
}
