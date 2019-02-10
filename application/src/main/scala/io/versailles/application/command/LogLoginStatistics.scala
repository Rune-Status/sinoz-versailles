package io.versailles.application.command

import io.versailles.application.model.ClientState

import scala.concurrent.duration.Duration

/**
  * A command to log the recorded statistical information about the
  * login procedure.
  * @author Sino
  */
case class LogLoginStatistics(
  timeDisconnected: Duration,
  timeTakenToProcessLogin: Duration,
  clientState: ClientState.Type,
  loginCount: Int
)
