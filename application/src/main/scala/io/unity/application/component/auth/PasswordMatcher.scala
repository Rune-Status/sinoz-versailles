package io.unity.application.component.auth

import io.unity.domain.model.Password

/**
  * TODO
  * @author Sino
  */
trait PasswordMatcher {
  def matched(plain: Password, hashed: Password): Boolean
}
