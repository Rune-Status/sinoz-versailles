package io.unity.domain.event

/**
  * An event of the text within a user interface label being updated.
  * @author Sino
  */
case class UILabelTextUpdated(
  interface: Int,
  component: Int,
  text: String
)
