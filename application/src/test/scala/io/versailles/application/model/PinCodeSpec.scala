package io.versailles.application.model

import org.scalatest.{FlatSpec, Matchers}

final class PinCodeSpec extends FlatSpec with Matchers {
  "A pin code" should "not be valid if it doesn't have exactly six digits" in {
    val pinCode = PinCode(4282)

    pinCode.isValid should be(false)
  }

  "A pin code" should "be valid if it consists of exactly six digits" in {
    val pinCode = PinCode(428219)

    pinCode.isValid should be(true)
  }
}
