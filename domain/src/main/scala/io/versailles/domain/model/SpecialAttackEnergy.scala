package io.versailles.domain.model

object SpecialAttackEnergy {
  /** The minimal and maximum amount of special attack energy percentages. */
  val MinPercentage = 0
  val MaxPercentage = 100

  /** The default initial [[SpecialAttackEnergy]] level. */
  val Default = SpecialAttackEnergy(percentage = 100)
}

/**
  * The energy level of the special attack bar.
  * @author Sino
  */
case class SpecialAttackEnergy(private val percentage: Int) extends AnyVal {
  def drain(energy: SpecialAttackEnergy) =
    copy(percentage = Math.max(percentage - energy.toPercentage, SpecialAttackEnergy.MinPercentage))

  def restore(energy: SpecialAttackEnergy) =
    copy(percentage = Math.min(percentage + energy.toPercentage, SpecialAttackEnergy.MaxPercentage))

  def toPercentage = percentage
}
