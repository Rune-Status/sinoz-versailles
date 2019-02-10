package io.versailles.domain.command

import io.versailles.domain.model.NpcId

/**
  * A command from a player to have an npc examined.
  * @author Sino
  */
case class ExamineNpc(npcId: NpcId)
