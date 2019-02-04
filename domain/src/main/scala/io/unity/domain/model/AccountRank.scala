package io.unity.domain.model

/**
  * The rank of an account e.g player moderator, staff.
  * @author Sino
  */
object AccountRank {
  case object PlayerMod extends Type
  case object Staff extends Type
  sealed abstract class Type
}
