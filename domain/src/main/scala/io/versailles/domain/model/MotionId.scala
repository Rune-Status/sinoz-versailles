package io.versailles.domain.model

/**
  * The id of a motion.
  * @author Sino
  */
case class MotionId(private val value: Int) extends AnyVal {
  def toValue = value
}