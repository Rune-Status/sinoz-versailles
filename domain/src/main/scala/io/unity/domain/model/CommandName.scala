package io.unity.domain.model

/**
  * The name of a command.
  * @author Sino
  */
case class CommandName(private val value: String) extends AnyVal {
  def toValue = value
}
