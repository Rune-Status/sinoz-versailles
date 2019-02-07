package io.unity.application.model

/**
  * The state of the client.
  * @author Sino
  */
object ClientState {
  case object LoggedOut extends Type
  case object LoggingIn extends Type
  case object InGame extends Type
  case object Reconnecting extends Type
  sealed abstract class Type
}
