package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder

/**
  * A [[ByteToMessageDecoder]] that translates a stream of bytes to any kind
  * of game command.
  * @author Sino
  */
final class GameCommandDecoder extends ByteToMessageDecoder {
  override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: java.util.List[AnyRef]) = {
    // TODO
  }
}
