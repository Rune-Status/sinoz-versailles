package io.versailles.application.model

object PinCode {
  val ExpectedDigitCount = 6
}

/**
  * A six digit pincode used for two step authentication.
  * @author Sino
  */
case class PinCode(private val value: Int) extends AnyVal {
  def isValid =
    digitCount == PinCode.ExpectedDigitCount

  /** Returns the amount of digits this [[PinCode]]'s [[value]] has. */
  private def digitCount =
    value
      .toString
      .length
}
