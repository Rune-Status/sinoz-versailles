package io.unity.domain.event

import io.unity.domain.model.{AccountRank, ChatText, DisplayName, IronMan}

/**
  * An event where a clan message was broadcasted to everyone within
  * the clan of subject.
  * @author Sino
  */
case class ClanMessageBroadcasted(
  sender: DisplayName,
  rank: Option[AccountRank.Type],
  ironMan: Option[IronMan.Type],
  chatText: ChatText
)
