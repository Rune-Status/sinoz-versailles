package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.versailles.application.event.ServiceResponded
import io.versailles.application.event.ServiceResponded.{NewLoginAccepted, ReconnectionAccepted}

/**
  * A [[ServiceResponseEncoder]] that encodes login service response
  * messages such as [[NewLoginAccepted]] and [[ReconnectionAccepted]].
  * @author Sino
  */
final class LoginResponseEncoder extends ServiceResponseEncoder {
  override def encode(message: ServiceResponded.Type, out: ByteBuf): Unit = {
    message match {
      case NewLoginAccepted(details) =>
        // TODO

      case ReconnectionAccepted(details) =>
        // TODO

      case otherwise =>
        super.encode(message, out)
    }
  }
}
