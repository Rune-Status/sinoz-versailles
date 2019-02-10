package io.versailles.application.component.account

import io.versailles.domain.model.Password

/**
  * Hashes a [[Password]].
  * @author Sino
  */
trait PasswordHasher {
  def hash(input: Password): Password
}
