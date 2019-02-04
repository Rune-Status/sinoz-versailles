package io.unity.domain.model

import io.unity.domain.model.Appearance._

object Appearance {
  /** The sex or gender presented from a player's appearance. */
  object Sex {
    case object Male extends Type
    case object Female extends Type
    sealed abstract class Type
  }

  /** The styles in a player's appearance. */
  case class Styles(
    hair: Int,
    beard: Int,
    torso: Int,
    arms: Int,
    hands: Int,
    legs: Int,
    feet: Int
  )

  /** The colours in a player's appearance. */
  case class Colours(
    hair: Int,
    torso: Int,
    legs: Int,
    feet: Int,
    skin: Int
  )

  /** A skull head icon. */
  object SkullIcon {
    case object WildernessSkull extends Type
    sealed abstract class Type
  }

  /** A prayer head icon. */
  object PrayerIcon {
    case object ProtectFromMelee extends Type
    case object ProtectFromRanged extends Type
    case object ProtectFromMagic extends Type
    case object Redemption extends Type
    case object Retribution extends Type
    case object Smite extends Type
    sealed abstract class Type
  }
}

/**
  * The appearance of a player.
  * @author Sino
  */
case class Appearance(
  sex: Sex.Type,
  styles: Styles,
  colours: Colours,
  skullIcon: Option[SkullIcon.Type],
  prayerIcon: Option[PrayerIcon.Type],
)
