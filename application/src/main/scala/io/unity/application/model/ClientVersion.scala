package io.unity.application.model

/**
  * The version of the client.
  * @author Sino
  */
case class ClientVersion(private val value: Int) extends AnyVal {
  def isUpToDateWith(other: ClientVersion) =
    value >= other.value

  def toValue = value
}
