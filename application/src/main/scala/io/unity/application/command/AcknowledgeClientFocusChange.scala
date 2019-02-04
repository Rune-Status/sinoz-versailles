package io.unity.application.command

/**
  * A command to acknowledge that the client has had its focus changed.
  * @author Sino
  */
object AcknowledgeClientFocusChange {
  case object Opened extends Type
  case object Minimized extends Type

  sealed abstract class Type
}
