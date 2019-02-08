package io.unity.domain.model

/**
  * Describes an npc.
  * @author Sino
  */
case class NpcDescriptor(
  id: NpcId,
  name: NpcName,
  examine: NpcTextDescription
)
