package io.unity.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class EnergySpec extends FlatSpec with Matchers {
  "Adding units to an energy level" should "not exceed 100%" in {
    val initial = Energy(8000)
    val updated = initial + 3000

    updated.toPercentages should equal(100)
  }

  "Removing units from an energy level" should "not exceed 0%" in {
    val initial = Energy.MaxUnits
    val updated = initial - 12000

    updated.toPercentages should equal(0)
  }
}
