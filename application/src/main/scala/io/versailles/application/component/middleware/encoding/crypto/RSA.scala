package io.versailles.application.component.middleware.encoding.crypto

import io.netty.buffer.ByteBuf

/** @author Sino */
trait RSA {
  final def rsaCrypt(in: ByteBuf)(exponent: BigInt, modulus: BigInt) = {
    val inData = new Array[Byte](in.readableBytes())
    in.readBytes(inData)

    val inInt = BigInt(inData)
    val outInt = inInt.modPow(exponent, modulus)

    val outData = outInt.toByteArray
    val out = in.alloc().buffer(outData.length)

    out.writeBytes(outData)
  }
}
