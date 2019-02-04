package io.unity.application.model

/**
  * A five digit pincode used for two step authentication.
  * @author Sino
  */
case class PinCode(private val value: Int) extends AnyVal {
  def isValid = {
    val justNumbers = value.toString.replaceAll("-", "").replaceAll("+", "")

    justNumbers.length > 0 && justNumbers.length <= 5
  }
}
