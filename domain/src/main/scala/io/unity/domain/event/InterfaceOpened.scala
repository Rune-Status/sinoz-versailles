package io.unity.domain.event

import io.unity.domain.model.{InterfaceId, InterfacePosition}

/**
  * An event of an interface being opened onto a user's user interface.
  * @author Sino
  */
case class InterfaceOpened(
  id: InterfaceId,
  position: InterfacePosition.Type
)
