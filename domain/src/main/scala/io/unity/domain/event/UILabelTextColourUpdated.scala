package io.unity.domain.event

import io.unity.domain.model.UILabelColour

/**
  * An event of the text colour within a user interface label being updated.
  * @author Sino
  */
case class UILabelTextColourUpdated(
  interface: Int,
  component: Int,
  colour: UILabelColour
)
