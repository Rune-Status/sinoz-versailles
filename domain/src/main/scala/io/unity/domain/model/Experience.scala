package io.unity.domain.model

object Experience {
  def computeLevelAtExperience(targetExperience: Experience): Level = {
    var points = 0
    var output = 0
    var level = Skill.MinLevel
    while (level <= Skill.MaxLevel) {
      points += Math.floor(level + 300.0D * Math.pow(2.0, level / 7.0)).toInt
      output = Math.floor(points / 4).toInt
      if (output >= (targetExperience.toValue + 1)) {
        return Level(level)
      }

      level += 1
    }

    Level(Skill.MaxLevel)
  }

  def computeExperienceAtLevel(targetLevel: Level): Experience = {
    var points = 0
    var output = 0
    var level = Skill.MinLevel
    while (level <= Skill.MaxLevel) {
      points += Math.floor(targetLevel.toValue + 300.0D * Math.pow(2.0, level / 7.0)).toInt
      if (level >= targetLevel.toValue) {
        return Experience(output)
      }

      output = Math.floor(points / 4).toInt
      level += 1
    }

    Experience(0)
  }

  val Max = Experience(200000000)
}

/**
  * The experience of a [[Skill]].
  * @author Sino
  */
case class Experience(private val value: Double) extends AnyVal {
  def +(gain: Experience) = {
    val newAmount = value + gain.value
    if (newAmount >= Experience.Max.value) {
      Experience.Max
    } else {
      copy(value = newAmount)
    }
  }

  def toValue = value
}
