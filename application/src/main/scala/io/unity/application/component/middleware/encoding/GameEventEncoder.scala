package io.unity.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
  * A [[MessageToByteEncoder]] that translates any kind of game event
  * to bytes to write to an outgoing stream.
  * @author Sino
  */
final class GameEventEncoder extends MessageToByteEncoder[Any] {
  override def encode(ctx: ChannelHandlerContext, msg: Any, out: ByteBuf) = {
    // TODO
  }
}
