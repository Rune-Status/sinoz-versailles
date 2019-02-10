package io.versailles.domain.event

import io.versailles.domain.model.LabelColour

/**
  * An event of the text colour within a user interface label being updated.
  * @author Sino
  */
case class LabelTextColourUpdated(
  interface: Int,
  component: Int,
  colour: LabelColour
)
