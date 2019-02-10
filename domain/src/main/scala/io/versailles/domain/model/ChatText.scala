package io.versailles.domain.model

/**
  * The text content within a chat message.
  * @author Sino
  */
case class ChatText(private val value: String) extends AnyVal {
  def toValue = value
}