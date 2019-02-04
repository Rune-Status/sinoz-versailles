package io.unity.domain.command

import io.unity.domain.model.PublicChatMessage

/**
  * A command that denotes that a user wishes to broadcast a chat message.
  * @author Sino
  */
case class BroadcastPublicChatMessage(message: PublicChatMessage)
