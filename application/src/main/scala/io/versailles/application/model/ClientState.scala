package io.versailles.application.model

/**
  * The state of the client.
  * @author Sino
  */
object ClientState {
  case object NewLogin extends Type
  case object Reconnecting extends Type
  case object LoggedIn extends Type
  case object LoggedOut extends Type
  sealed abstract class Type
}
