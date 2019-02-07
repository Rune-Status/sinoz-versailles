package io.unity.application.component.auth

import io.unity.domain.model.Password
import org.mindrot.jbcrypt.BCrypt

/**
  * A [[PasswordMatcher]] that uses the BCrypt hashing implementation
  * to check if two [[Password]]s match.
  * @author Sino
  */
final class BCryptPasswordMatcher extends PasswordMatcher {
  override def matched(plain: Password, hashed: Password) =
    BCrypt.checkpw(plain.toValue, hashed.toValue)
}
