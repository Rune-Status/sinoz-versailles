package io.unity.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class EmailSpec extends FlatSpec with Matchers {
  "An email address" should "contain an @ sign" in {
    val email = Email("john@google.nl")

    email.isValid should be(true)
  }

  "An email address" should "disallow a missing @ sign" in {
    val email = Email("johngoogle.nl")
    email.isValid should be(false)
  }

  "An email address" should "contain a name before the @ sign" in {
    val email = Email("@google.uk")
    email.isValid should be(false)
  }

  "An email address" should "disallow a missing domain name" in {
    val email = Email("john@.uk")
    email.isValid should be(false)
  }

  "An email address" should "disallow a missing domain extension" in {
    val email = Email("john@google")
    email.isValid should be(false)
  }

  "An email address" should "contain a name, @ sign, domain name and extension" in {
    val email = Email("johndoe@yahoo.com")

    email.isValid should be(true)
  }

  "An email address" should "not contain special characters in the name, domain name and extension" in {
    val email = Email("%#ws@gmai473.com#")

    email.isValid should be(false)
  }
}
