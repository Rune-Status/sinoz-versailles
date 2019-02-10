package io.versailles.application.command

import io.versailles.application.model.ClientState

/**
  * A command to acknowledge that the state of the client has been updated
  * during serving of the RuneScape game cache.
  * @author Sino
  */
case class AcknowledgeClientStateChange(state: ClientState.Type)
