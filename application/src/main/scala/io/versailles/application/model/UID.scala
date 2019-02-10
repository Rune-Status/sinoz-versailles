package io.versailles.application.model

/**
  * A unique identifier.
  * @author Sino
  */
case class UID(private val values: Seq[Int]) extends AnyVal {
  def toValue = values
}
