package io.unity.domain.model

import io.unity.domain.model.NpcDescriptor.MonsterBonuses

object NpcDescriptor {
  /** The combat bonuses of a monster. */
  case class MonsterBonuses(
    aggressive: Map[CombatBonus.Aggressive.Type, CombatBonus],
    defensive: Map[CombatBonus.Defensive.Type, CombatBonus],
    other: Map[CombatBonus.Other.Type, CombatBonus],
  )
}

/**
  * Describes an npc. An npc may be a monster which can be fought against
  * and may therefore have combat skills and bonuses.
  * @author Sino
  */
case class NpcDescriptor(
  id: NpcId,
  name: NpcName,

  skills: Option[Vector[Skill]],
  bonuses: Option[MonsterBonuses],

  clickable: Boolean,
  showOnMinimap: Boolean,

  examine: NpcTextDescription
)
