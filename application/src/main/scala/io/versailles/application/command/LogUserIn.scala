package io.versailles.application.command

import io.versailles.application.model.PinCodeInput
import io.versailles.domain.model.{Email, Password}

/**
  * A command to log a user into the game world.
  * @author Sino
  */
// TODO move to domain, what about pin code input? this is relevant for authentication which is an application detail
case class LogUserIn(
  email: Email,
  password: Password,
  pinCodeInput: Option[PinCodeInput.Type],
)
