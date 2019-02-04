package io.unity.application.model

import java.security.SecureRandom

object Nonce {
  private val random = new SecureRandom()

  def generate = Nonce(random.nextLong())
}

/**
  * A cryptographic arbitrary number that is used once.
  * @author Sino
  */
case class Nonce(private val value: Long) extends AnyVal {
  def isValid = value != 0

  def toValue = value
}
