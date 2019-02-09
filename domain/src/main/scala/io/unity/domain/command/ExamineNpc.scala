package io.unity.domain.command

import io.unity.domain.model.NpcId

/**
  * A command from a player to have an npc examined.
  * @author Sino
  */
case class ExamineNpc(npcId: NpcId)
