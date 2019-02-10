package io.versailles.application.command

import java.net.Inet4Address

import io.versailles.domain.model.{WorldId, WorldSetting}

/**
  * A command to let the client switch to another game world.
  * @author Sino
  */
case class SwitchWorld(
  address: Inet4Address,
  worldId: WorldId,
  settings: Set[WorldSetting.Type]
)