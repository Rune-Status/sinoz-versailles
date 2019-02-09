package io.unity.domain.event

import io.unity.domain.model.{AccountRank, ChatText, DisplayName, IronMan}

/**
  * An event where a private chat message was sent from one user to another.
  * @author Sino
  */
case class PrivateMessageSent(
  sender: DisplayName,
  recipient: DisplayName,
  rank: Option[AccountRank.Type],
  ironMan: Option[IronMan.Type],
  chatText: ChatText
)
