package io.unity.domain.model

/**
  * A PID is short for process identifier, which is used to identify active
  * processes in the game such as players and npcs.
  * @author Sino
  */
case class PID(private val value: Int) extends AnyVal {
  def toValue = value
}
