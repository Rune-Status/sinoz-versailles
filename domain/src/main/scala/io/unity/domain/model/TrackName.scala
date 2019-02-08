package io.unity.domain.model

/**
  * The name of a music track.
  * @author Sino
  */
case class TrackName(private val value: String) extends AnyVal {
  def toValue = value
}