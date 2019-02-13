package io.versailles.domain.event

/**
  * An event of a login attempt not being able to be completed under
  * any circumstance.
  * @author Sino
  */
case class CouldNotCompleteLogin(cause: Throwable) extends Throwable
