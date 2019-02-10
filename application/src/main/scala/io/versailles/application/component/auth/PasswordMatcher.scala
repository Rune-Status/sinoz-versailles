package io.versailles.application.component.auth

import io.versailles.domain.model.Password

/**
  * Matches a given plain [[Password]] with its hashed variant.
  * @author Sino
  */
trait PasswordMatcher {
  def matched(plain: Password, hashed: Password): Boolean
}
