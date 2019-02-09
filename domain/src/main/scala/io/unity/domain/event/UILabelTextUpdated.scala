package io.unity.domain.event

import io.unity.domain.model.UILabelText

/**
  * An event of a [[UILabelText]] being updated.
  * @author Sino
  */
case class UILabelTextUpdated(
  interface: Int,
  component: Int,
  text: UILabelText
)
