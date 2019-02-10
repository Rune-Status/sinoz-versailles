package io.versailles.application.model

import io.versailles.domain.model.{Email, Machine, Password, ScreenResolution}

/**
  * A client user's request to log into a game world.
  * @author Sino
  */
case class LoginRequest(
  email: Email,
  password: Option[Password],
  clientState: ClientState.Type,
  nonce: Nonce,
  pinCodeInput: Option[PinCodeInput.Type],
  seeds: Seq[Seed],
  previousSeeds: Option[Seq[Seed]],
  archiveChecksums: Seq[ArchiveChecksum],
  clientVersion: ClientVersion,
  screenResolution: ScreenResolution,
  uid: UID,
  machine: Machine
)