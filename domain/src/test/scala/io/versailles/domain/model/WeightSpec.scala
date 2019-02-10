package io.versailles.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class WeightSpec extends FlatSpec with Matchers {
  "Weight" should "be able to be lost" in {
    val weight = Weight.Default
    val lost = weight.lose(25)

    lost should equal(Weight(-25))
  }

  "An actor's weight" should "be able to be gained" in {
    val weight = Weight.Default
    val gained = weight.gain(50)

    gained should equal(Weight(50))
  }
}
