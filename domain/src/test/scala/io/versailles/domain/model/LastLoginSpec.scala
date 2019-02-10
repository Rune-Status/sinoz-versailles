package io.versailles.domain.model

import java.time.LocalDate

import org.scalatest.{FlatSpec, Matchers}

final class LastLoginSpec extends FlatSpec with Matchers {
  "The difference between a last login and the current time" should "be calculated in days" in {
    val today = LocalDate.now()
    val lastLogin = LastLogin(today.minusDays(5))

    lastLogin.daysSince(today) should equal(5)
  }
}
