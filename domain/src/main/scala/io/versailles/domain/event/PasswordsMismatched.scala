package io.versailles.domain.event

/**
  * An event of a stored password and a password from user input
  * having a mismatch.
  * @author Sino
  */
case object PasswordsMismatched extends Throwable
