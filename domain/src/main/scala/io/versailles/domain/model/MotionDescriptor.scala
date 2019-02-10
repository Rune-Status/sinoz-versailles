package io.versailles.domain.model

import scala.concurrent.duration.Duration

/**
  * Describes a game motion.
  * @author Sino
  */
case class MotionDescriptor(
  id: MotionId,
  duration: Duration
)
