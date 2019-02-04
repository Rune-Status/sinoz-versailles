package io.unity.domain.model

/**
  * A type of movement across the game map.
  * @author Sino
  */
object Movement {
  case object Walk extends Type
  case object Run extends Type
  case object Teleport extends Type
  sealed abstract class Type

  /** Staff users are allowed to use keyboard shortcuts to force an act of
    * running or teleportation, potentially bending the rules. */
  object Cheat {
    case object CtrlRunning extends Type
    case object CtrlShiftTeleport extends Type
    sealed abstract class Type
  }
}
