package io.versailles.application.command

import io.versailles.application.model._

/**
  * A command to log into a game world.
  * @author Sino
  */
case class RequestLogin(
  clientState: ClientState.Type,
  request: LoginRequest
)
