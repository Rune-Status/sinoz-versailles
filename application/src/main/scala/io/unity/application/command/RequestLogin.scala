package io.unity.application.command

import io.unity.application.model._
import io.unity.domain.model.{Email, Machine, Password}

/**
  * A client's command to log into a game world.
  * @author Sino
  */
case class RequestLogin(
  email: Email,
  password: Option[Password],
  clientState: ClientState.Type,
  pinCodeInput: Option[PinCodeInput.Type],
  seeds: Seq[Seed],
  previousSeeds: Seq[Seed],
  archiveChecksums: Seq[ArchiveChecksum],
  clientVersion: ClientVersion,
  uid: UID,
  machine: Machine
)
