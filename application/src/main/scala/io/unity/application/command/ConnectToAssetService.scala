package io.unity.application.command

import io.unity.application.model.ClientVersion

/**
  * A client command to connect to the game world's asset service.
  * @author Sino
  */
case class ConnectToAssetService(version: ClientVersion)
