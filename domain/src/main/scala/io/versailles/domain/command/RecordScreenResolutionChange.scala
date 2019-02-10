package io.versailles.domain.command

import io.versailles.domain.model.ScreenResolution

/**
  * A command that denotes that the client has had its screen resolution
  * updated and must therefore be synchronized with the state of the server.
  * @author Sino
  */
case class RecordScreenResolutionChange(updated: ScreenResolution)
