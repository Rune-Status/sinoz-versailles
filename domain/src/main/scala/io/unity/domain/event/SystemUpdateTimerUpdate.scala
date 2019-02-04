package io.unity.domain.event

import scala.concurrent.duration.Duration

/**
  * An event of the system update timer being updated.
  * @author Sino
  */
case class SystemUpdateTimerUpdate(duration: Duration)
