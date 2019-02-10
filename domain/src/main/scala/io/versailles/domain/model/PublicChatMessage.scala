package io.versailles.domain.model

object PublicChatMessage {
  /** The effect to apply to the chat message. */
  object Effect {
    sealed abstract class Type
  }

  /** The colour to apply to the chat message. */
  object Colour {
    sealed abstract class Type
  }
}

/**
  * A chat message that is destined for the nearby players.
  * @author Sino
  */
case class PublicChatMessage(
  text: ChatText,
  effect: Option[PublicChatMessage.Effect.Type],
  colour: Option[PublicChatMessage.Colour.Type]
)
