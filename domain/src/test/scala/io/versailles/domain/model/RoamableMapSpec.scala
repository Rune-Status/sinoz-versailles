package io.versailles.domain.model

import org.scalatest.{FlatSpec, Matchers}

final class RoamableMapSpec extends FlatSpec with Matchers {
  "A roamable map" should "require a rebuild when a lower edge is reached" in {
    val map = RoamableMap(Position(3222, 3222, 0))
    val lowerEdge = Position(3222 - 40, 3222, 0)

    val requireRebuild = map.reachedEdge(lowerEdge)

    requireRebuild should equal(true)
  }

  "A roamable map" should "not require a rebuild when near the center" in {
    val map = RoamableMap(Position(3222, 3222, 0))
    val nearCenter = Position(3230, 3210, 0)

    val requireRebuild = map.reachedEdge(nearCenter)

    requireRebuild should equal(false)
  }

  "A roamable map" should "require a rebuild when an upper edge is reached" in {
    val map = RoamableMap(Position(3222, 3222, 0))
    val upperEdge = Position(3222 + 36, 3222, 0)

    val requireRebuild = map.reachedEdge(upperEdge)

    requireRebuild should equal(true)
  }
}
