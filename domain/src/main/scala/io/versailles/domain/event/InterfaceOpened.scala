package io.versailles.domain.event

import io.versailles.domain.model.{InterfaceId, InterfacePosition}

/**
  * An event of an interface being opened onto a user's user interface.
  * @author Sino
  */
case class InterfaceOpened(
  id: InterfaceId,
  position: InterfacePosition.Type
)
