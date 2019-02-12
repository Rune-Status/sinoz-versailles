package io.versailles.application.event

import java.net.Inet4Address

import io.versailles.domain.model.{WorldId, WorldSetting}

/**
  * An event of a user switching between worlds.
  * @author Sino
  */
case class WorldSwitched(
  address: Inet4Address,
  worldId: WorldId,
  settings: Set[WorldSetting.Type]
)
