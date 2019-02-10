package io.versailles.domain.model

object Rotation {
  val NoRotation = Rotation(0)
  val By90Degrees = Rotation(90)
  val By180Degrees = Rotation(180)
  val By270Degrees = Rotation(270)
}

/**
  * A rotation in degrees.
  * @author Sino
  */
case class Rotation(private val degrees: Int) extends AnyVal
