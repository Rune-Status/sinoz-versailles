package io.unity.application.component.auth

import io.unity.domain.model.Password
import org.mindrot.jbcrypt.BCrypt
import org.scalatest.{FlatSpec, Matchers}

final class BCryptPasswordMatcherSpec extends FlatSpec with Matchers {
  "A BCryptPasswordMatcher" should "be able to match two equal passwords regardless of the hashing" in {
    val matcher = new BCryptPasswordMatcher

    val hashed = Password(BCrypt.hashpw("hello_world", BCrypt.gensalt()))
    val plain = Password("hello_world")

    matcher.matched(plain, hashed) should be(true)
  }

  "A BCryptPasswordMatcher" should "be able to notice a difference in two unequal passwords regardless of the hashing" in {
    val matcher = new BCryptPasswordMatcher

    val hashed = Password(BCrypt.hashpw("helloworld", BCrypt.gensalt()))
    val plain = Password("hello_world")

    matcher.matched(plain, hashed) should be(false)
  }
}
