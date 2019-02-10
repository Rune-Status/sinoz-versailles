package io.versailles.application.component.account

import io.versailles.domain.model.Password
import org.mindrot.jbcrypt.BCrypt

/**
  * A [[PasswordHasher]] that uses the BCrypt hashing implementation
  * to resolve a given [[Password]] to its hashed variant.
  * @author Sino
  */
final class BCryptPasswordHasher extends PasswordHasher {
  override def hash(input: Password) = {
    val passwordSalt = BCrypt.gensalt()
    val hashedVariant = BCrypt.hashpw(input.toValue, passwordSalt)

    Password(hashedVariant)
  }
}
