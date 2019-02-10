package io.versailles.domain.model

object LabelColour {
  // TODO more colours

  val Black = LabelColour(r = 0, g = 0, b = 0)
}

/**
  * The colour of a user interface label. The colour is encoded in RGB.
  * @author Sino
  */
case class LabelColour(r: Int, g: Int, b: Int)
