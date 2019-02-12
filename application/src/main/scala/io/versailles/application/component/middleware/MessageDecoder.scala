package io.versailles.application.component.middleware

import io.netty.buffer.ByteBuf

object MessageDecoder {
  object Result {
    case class MessageOutput(message: Any) extends Type
    case object RequireMoreBytes extends Type
    sealed abstract class Type
  }
}

/**
  * Decodes the contents of a given [[ByteBuf]] to an ADT.
  * @author Sino
  */
trait MessageDecoder {
  def decode(in: ByteBuf): MessageDecoder.Result.Type
}
