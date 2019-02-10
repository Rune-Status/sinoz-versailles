package io.versailles.domain.event

import io.versailles.domain.model.{InterfaceId, InterfacePosition}

/**
  * An event of an interface being closed and removed from a user's
  * user interface.
  * @author Sino
  */
case class InterfaceClosed(
  parent: InterfaceId,
  positionRemovedFrom: InterfacePosition.Type
)
