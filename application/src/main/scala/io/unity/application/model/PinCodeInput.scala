package io.unity.application.model

/**
  * The type of pin code input during login.
  * @author Sino
  */
object PinCodeInput {
  case class EnteredPinCode(pinCode: PinCode, trustFor30Days: Boolean) extends Type
  case class StoredPinCodeHash(hash: Int) extends Type
  sealed abstract class Type
}
