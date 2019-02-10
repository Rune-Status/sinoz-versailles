package io.versailles.application.command

import io.versailles.application.model.ClientVersion

/**
  * A client command to connect to the game world's asset service.
  * @author Sino
  */
case class ConnectToAssetService(version: ClientVersion)
