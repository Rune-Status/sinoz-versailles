package io.unity.application.command

import io.unity.application.model.ClientState

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
