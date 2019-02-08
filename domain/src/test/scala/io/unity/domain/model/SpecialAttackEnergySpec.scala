package io.unity.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class SpecialAttackEnergySpec extends FlatSpec with Matchers {
  "The energy of a special attack bar" should "be able to be restored" in {
    val energy = SpecialAttackEnergy(percentage = 50)
    val restored = energy.restore(25)

    restored should equal(SpecialAttackEnergy(75))
  }

  "The energy of a special attack bar" should "be able to be drained" in {
    val energy = SpecialAttackEnergy(percentage = 50)
    val drained = energy.drain(25)

    drained should equal(SpecialAttackEnergy(25))
  }

  "The energy in percentages of a special attack bar when restored" should "not exceed 100%" in {
    val energy = SpecialAttackEnergy(percentage = 80)
    val restored = energy.restore(30)

    restored should equal(SpecialAttackEnergy(100))
  }

  "The energy in percentages of a special attack bar when drained" should "not exceed 0%" in {
    val energy = SpecialAttackEnergy(percentage = 50)
    val drained = energy.drain(80)

    drained should equal(SpecialAttackEnergy(0))
  }
}
