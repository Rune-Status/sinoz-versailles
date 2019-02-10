package io.versailles.application.model

/**
  * An RSA key pair that is used to decrypt the confidential block of
  * user credentials during the decoding process of the [[LoginRequest]].
  * @author Sino
  */
case class CredentialBlockKeySet(
  modulus: BigInt,
  exponent: BigInt
)
