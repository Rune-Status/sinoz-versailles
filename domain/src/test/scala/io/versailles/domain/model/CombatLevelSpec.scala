package io.versailles.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class CombatLevelSpec extends FlatSpec with Matchers {
  "A combat level" should "start at level 1 when all combat skills are level 1" in {
    val skillSet = Vector.tabulate(7)(id => Skill(id, Level(1), Level(1), Experience(0D)))
    val combatLevel = CombatLevel.evaluate(skillSet)

    combatLevel should equal(CombatLevel(Level(1)))
  }

  "A combat level" should "cap at level 126 when all combat skills are level 99" in {
    val skillSet = Vector.tabulate(7)(id => Skill(id, Level(99), Level(99), Experience(0D)))
    val combatLevel = CombatLevel.evaluate(skillSet)

    combatLevel should equal(CombatLevel(Level(126)))
  }

  "A combat level" should "only be affected by max levels and not by normal levels" in {
    val skillSet = Vector.tabulate(7)(id => Skill(id, Level(1), Level(99), Experience(0D)))
    val combatLevel = CombatLevel.evaluate(skillSet)

    combatLevel should equal(CombatLevel(Level(126)))
  }
}
