package io.versailles.domain.model

object Weight {
  val Default = Weight(kilos = 0)
}

/**
  * The weight of an actor on the game world. The weight of an actor may
  * drastically reduce the performance of an actor. A player for instance,
  * for instance may carry a lot of items and ingame and thus have
  * its stamina reduced at a faster rate than usual when running.
  * @author Sino
  */
case class Weight(private val kilos: Float) extends AnyVal {
  def lose(amountInKilos: Float) = copy(kilos = kilos - amountInKilos)

  def gain(amountInKilos: Float) = copy(kilos = kilos + amountInKilos)

  def toKilos = kilos
}
