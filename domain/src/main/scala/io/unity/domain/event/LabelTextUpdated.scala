package io.unity.domain.event

import io.unity.domain.model.LabelText

/**
  * An event of a [[LabelText]] being updated.
  * @author Sino
  */
case class LabelTextUpdated(
  interface: Int,
  component: Int,
  text: LabelText
)
