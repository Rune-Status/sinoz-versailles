package io.unity.domain.model

/**
  * A prayer head icon.
  * @author Sino
  */
object PrayerIcon {
  case object ProtectFromMelee extends Type
  case object ProtectFromRanged extends Type
  case object ProtectFromMagic extends Type
  case object Redemption extends Type
  case object Retribution extends Type
  case object Smite extends Type
  sealed abstract class Type
}