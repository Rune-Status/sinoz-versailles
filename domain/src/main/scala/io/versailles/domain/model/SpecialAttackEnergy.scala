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
  def drain(amount: Int) =
    copy(percentage = Math.max(percentage - amount, SpecialAttackEnergy.MinPercentage))

  def restore(amount: Int) =
    copy(percentage = Math.min(percentage + amount, SpecialAttackEnergy.MaxPercentage))

  def toPercentage = percentage
}
