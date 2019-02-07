package io.unity.application.component.account

import io.unity.domain.model.Password

/**
  * Hashes a [[Password]].
  * @author Sino
  */
trait PasswordHasher {
  def hash(input: Password): Password
}
