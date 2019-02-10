package io.versailles.domain.model

/**
  * An argument passed in a game command to further clarify the what, where
  * or how in a game command.
  * @author Sino
  */
case class CommandArgument(private val value: String) extends AnyVal {
  def asInt = value.toInt

  def toValue = value
}
