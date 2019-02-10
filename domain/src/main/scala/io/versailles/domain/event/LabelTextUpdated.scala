package io.versailles.domain.event

import io.versailles.domain.model.LabelText

/**
  * An event of a [[LabelText]] being updated.
  * @author Sino
  */
case class LabelTextUpdated(
  interface: Int,
  component: Int,
  text: LabelText
)
