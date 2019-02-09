package io.unity.domain.event

import io.unity.domain.model.LabelColour

/**
  * An event of the text colour within a user interface label being updated.
  * @author Sino
  */
case class LabelTextColourUpdated(
  interface: Int,
  component: Int,
  colour: LabelColour
)
