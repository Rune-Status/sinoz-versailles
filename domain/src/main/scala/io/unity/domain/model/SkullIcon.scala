package io.unity.domain.model

/**
  * A skull head icon.
  * @author Sino
  */
object SkullIcon {
  case object WildernessSkull extends Type
  sealed abstract class Type
}