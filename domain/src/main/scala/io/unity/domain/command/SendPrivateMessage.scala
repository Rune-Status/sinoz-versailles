package io.unity.domain.command

import io.unity.domain.model.PrivateChatMessage

/**
  * A command to send another user a private game message.
  * @author Sino
  */
case class SendPrivateMessage(message: PrivateChatMessage)
