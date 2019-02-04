package io.unity.application.model

import io.unity.domain.model.{Email, Machine, Password}

/**
  * A client's request to log into the game world.
  * @author Sino
  */
case class LoginRequest(
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
