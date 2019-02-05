package io.unity.domain.model

object Experience {
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
      copy(value = value + gain.value)
    }
  }

  def toValue = value
}
