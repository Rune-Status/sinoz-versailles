package io.unity.domain.command

import java.awt.Point

/**
  * A command to synchronize the [[Point]] to where the client has had its
  * perspective camera moved towards, with the state of the server.
  * @author Sino
  */ // TODO better naming
case class SyncMovedPerspectiveCamera(point: Point)
