package io.versailles.application.command

import io.versailles.application.model.FPS

import scala.concurrent.duration.Duration

/**
  * A command to log the recorded statistical information about the client's
  * performance during gameplay.
  * @author Sino
  */
case class LogGameStatistics(
  fps: FPS,
  gcTime: Duration
)
