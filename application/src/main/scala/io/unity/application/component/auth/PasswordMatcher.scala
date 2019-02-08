package io.unity.application.component.auth

import io.unity.domain.model.Password

/**
  * Matches a given plain [[Password]] with its hashed variant.
  * @author Sino
  */
trait PasswordMatcher {
  def matched(plain: Password, hashed: Password): Boolean
}
