package io.versailles.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class TotalLevelSpec extends FlatSpec with Matchers {
  "A total level" should "be the sum of the levels of the set of skills it is given" in {
    val skillSet = Vector.tabulate(23)(id => Skill(id, Level(1), Level(1), Experience(0D)))
    val totalLevel = TotalLevel.evaluate(skillSet)

    totalLevel should equal(TotalLevel(Level(23)))
  }
}
