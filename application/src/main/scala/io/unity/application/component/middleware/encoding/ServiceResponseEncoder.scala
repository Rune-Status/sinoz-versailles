package io.unity.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.unity.application.model.ServiceResponse
import io.unity.application.model.ServiceResponse.MayProceed

/**
  * A [[MessageToByteEncoder]] that translates [[ServiceResponse]]s
  * to bytes to write to an outgoing stream.
  * @author Sino
  */
class ServiceResponseEncoder extends MessageToByteEncoder[ServiceResponse.Type] {
  override def encode(ctx: ChannelHandlerContext, msg: ServiceResponse.Type, out: ByteBuf) = {
    out.writeByte(msg.code)

    msg match {
      case MayProceed(nonceOpt) =>
        nonceOpt.foreach(nonce => out.writeLong(nonce.toValue))

      case otherwise =>
        // nothing
    }
  }
}
