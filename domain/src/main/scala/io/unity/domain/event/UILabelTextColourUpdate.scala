package io.unity.domain.event

/**
  * An event of the text colour within a user interface label being updated.
  * @author Sino
  */
case class UILabelTextColourUpdate(
  interface: Int,
  component: Int,
  r: Int,
  g: Int,
  b: Int
)
