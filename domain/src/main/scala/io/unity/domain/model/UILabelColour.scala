package io.unity.domain.model

object UILabelColour {
  // TODO more colours

  val Black = UILabelColour(r = 0, g = 0, b = 0)
}

/**
  * The colour of a user interface label. The colour is encoded in RGB.
  * @author Sino
  */
case class UILabelColour(r: Int, g: Int, b: Int)
