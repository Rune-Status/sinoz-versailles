package io.unity.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.unity.application.event.ServiceResponse
import io.unity.application.event.ServiceResponse.{NewLoginAccepted, ReconnectionAccepted}

/**
  * A [[ServiceResponseEncoder]] that can also translate login specific
  * events such as [[NewLoginAccepted]] and [[ReconnectionAccepted]] to
  * bytes to write to an outgoing stream.
  * @author Sino
  */
final class LoginResponseEncoder extends ServiceResponseEncoder {
  override def encode(ctx: ChannelHandlerContext, msg: ServiceResponse.Type, out: ByteBuf): Unit = {
    msg match {
      case NewLoginAccepted(details) =>
        // TODO

      case ReconnectionAccepted(details) =>
        // TODO

      case otherwise =>
        super.encode(ctx, msg, out)
    }
  }
}
