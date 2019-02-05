package io.unity.domain.model

object TotalLevel {
  /** Evaluates the given [[Vector]] of [[Skill]]s to compute the
    * [[TotalLevel]]. */
  def evaluate(set: Vector[Skill]) =
    TotalLevel(Level(set
      .map(_.maxLevel)
      .map(_.toValue)
      .sum))
}

/**
  * A sum of levels of each and every [[Skill]].
  * @author Sino
  */
case class TotalLevel(private val level: Level) {
  def reevaluate(set: Vector[Skill]) = TotalLevel.evaluate(set)

  def toValue = level.toValue
}
