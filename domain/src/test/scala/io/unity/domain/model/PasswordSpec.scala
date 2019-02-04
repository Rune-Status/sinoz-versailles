package io.unity.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class PasswordSpec extends FlatSpec with Matchers {
  "A password" should "be invalid when missing at least a single digit" in {
    val password = Password("HelloWorld#")
    password.isValid should equal(false)
  }

  "A password" should "be invalid when missing at least a single lower case letter" in {
    val password = Password("HELLO_WORLD21#")
    password.isValid should equal(false)
  }

  "A password" should "be invalid when missing at least a single upper case letter" in {
    val password = Password("hello_world#23")
    password.isValid should equal(false)
  }

  "A password" should "not allow any whitespace" in {
    val password = Password("hello world#23")
    password.isValid should equal(false)
  }

  "A password" should "only be valid if it contains at least a single special character" in {
    val password = Password("hello world2289")
    password.isValid should equal(false)
  }

  "A password" should "be at least six characters" in {
    val password = Password("#23hE")
    password.isValid should equal(false)
  }
}
