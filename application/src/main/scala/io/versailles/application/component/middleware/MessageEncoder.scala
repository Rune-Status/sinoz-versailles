package io.versailles.application.component.middleware

import io.netty.buffer.ByteBuf

/**
  * Encodes an ADT to a given [[ByteBuf]].
  * @author Sino
  */
trait MessageEncoder[R] {
  def encode(message: R, out: ByteBuf)
}
