package io.versailles.domain.event

import scala.concurrent.duration.Duration

/**
  * An event of the system update timer being updated.
  * @author Sino
  */
case class SystemUpdateTimerUpdated(duration: Duration)
