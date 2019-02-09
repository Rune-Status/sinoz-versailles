package io.unity.domain.model

/**
  * A private chat message to send from one user to another.
  * @author Sino
  */
case class PrivateChatMessage(
  recipient: DisplayName,
  text: ChatText
)
