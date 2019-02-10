package io.versailles.domain.command

import io.versailles.domain.model.PrivateChatMessage

/**
  * A command to send another user a private game message.
  * @author Sino
  */
case class SendPrivateMessage(message: PrivateChatMessage)
