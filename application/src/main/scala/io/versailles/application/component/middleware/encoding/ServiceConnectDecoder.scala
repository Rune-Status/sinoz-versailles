package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.versailles.application.command.{ConnectToAssetService, ConnectToLoginService}
import io.versailles.application.component.middleware.MessageDecoder
import io.versailles.application.component.middleware.MessageDecoder.Result.{MessageOutput, RequireMoreBytes}
import io.versailles.application.model.ClientVersion

/**
  * A [[MessageDecoder]] that decodes either a [[ConnectToAssetService]]
  * message or a [[ConnectToLoginService]], depending on the directive
  * it reads.
  * @author Sino
  */
final class ServiceConnectDecoder extends MessageDecoder {
  override def decode(in: ByteBuf): MessageDecoder.Result.Type = {
    val directive = in.readUnsignedByte()

    directive match {
      case 14 =>
        MessageOutput(ConnectToLoginService)

      case 15 =>
        if (in.readableBytes() < 4) {
          return RequireMoreBytes
        }

        MessageOutput(ConnectToAssetService(ClientVersion(in.readInt())))

      case otherwise =>
        throw new Exception(s"unexpected directive of value $otherwise")
    }
  }
}
