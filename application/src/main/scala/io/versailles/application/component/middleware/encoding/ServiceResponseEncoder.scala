package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.versailles.application.component.middleware.MessageEncoder
import io.versailles.application.event.ServiceResponded
import io.versailles.application.event.ServiceResponded.MayProceed

/**
  * A [[MessageEncoder]] that encodes [[ServiceResponded]]s.
  * @author Sino
  */
class ServiceResponseEncoder extends MessageEncoder[ServiceResponded.Type] {
  override def encode(message: ServiceResponded.Type, out: ByteBuf): Unit = {
    out.writeByte(message.code)

    message match {
      case MayProceed(nonceOpt) =>
        nonceOpt.foreach(nonce => out.writeLong(nonce.toValue))

      case otherwise =>
        // nothing
    }
  }
}
