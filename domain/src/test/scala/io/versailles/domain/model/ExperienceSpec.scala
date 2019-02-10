package io.versailles.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class ExperienceSpec extends FlatSpec with Matchers {
  "A skill's experience" should "not exceed the limit of 200 million" in {
    val experience = Experience(190000000) // 190 million
    val gain = Experience(11000000) // 11 million

    (experience + gain) should equal(Experience.Max)
  }
}
