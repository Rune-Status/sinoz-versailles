package io.versailles.application.tools

import java.security._
import java.security.spec.{RSAPrivateKeySpec, RSAPublicKeySpec}

/** @author Sino */
object RsaKeyGenTool {
  val KeyBitSize = 1024

  def main(args: Array[String]): Unit = {
    val factory = KeyFactory.getInstance("RSA")
    val keyGen = KeyPairGenerator.getInstance("RSA")

    keyGen.initialize(KeyBitSize)

    val keyPair = keyGen.genKeyPair
    val privKey = keyPair.getPrivate
    val pubKey = keyPair.getPublic

    val privSpec = factory.getKeySpec(privKey, classOf[RSAPrivateKeySpec])
    val pubSpec = factory.getKeySpec(pubKey, classOf[RSAPublicKeySpec])

    println(s"Private mod: ${privSpec.getModulus}")
    println(s"Private exp: ${privSpec.getPrivateExponent}")

    println(s"Public exp ${pubSpec.getPublicExponent}")
    println(s"Public mod: ${pubSpec.getModulus}")
  }
}
