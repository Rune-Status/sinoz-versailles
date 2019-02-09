package io.unity.domain.model

object SystemNotification {
  case object Regular extends Type
  case object TradeRequest extends Type
  case object Filterable extends Type
  case object Announcement extends Type
  sealed abstract class Type
}

/**
  * A system notification that is displayed in a player's chatbox.
  * @author Sino
  */
case class SystemNotification(
  notificationType: SystemNotification.Type,
  interaction: Option[DisplayName],
  chatText: ChatText
)
