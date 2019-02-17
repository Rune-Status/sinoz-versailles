package io.versailles.domain.model

object Energy {
  val MinUnits = Energy(0)
  val MaxUnits = Energy(10000)

  def fromPercentage(percentage: Int) =
    Energy(percentage * 100)
}

/**
  * An energy level represented as units. A unit is the 1/100th of an energy
  * level in percentages. To convert units to a percentage, divide by 100.
  * @author Sino
  */
case class Energy(private val units: Int) extends AnyVal {
  def +(energy: Energy) =
    copy(units = Math.min(units + energy.toUnits, Energy.MaxUnits.units))

  def -(energy: Energy) =
    copy(units = Math.max(units - energy.toUnits, Energy.MinUnits.units))

  def toUnits = units

  def toPercentages = units / 100
}
