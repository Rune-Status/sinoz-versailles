package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.versailles.application.command.{ConnectToAssetService, ConnectToLoginService}
import io.versailles.application.model.ClientVersion

/**
  * A [[ByteToMessageDecoder]] that translates a stream of bytes into
  * a [[ConnectToAssetService]] message or a [[ConnectToLoginService]]
  * message, depending on the initial directive.
  * @author Sino
  */
final class ServiceConnectDecoder extends ByteToMessageDecoder {
  /** The directives of a service connect message. */
  val LoginServiceId = 14
  val AssetServiceId = 15

  override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: java.util.List[AnyRef]): Unit = {
    in.markReaderIndex()

    val directive = in.readUnsignedByte()

    directive match {
      case LoginServiceId =>
        out.add(ConnectToLoginService)

      case AssetServiceId =>
        if (in.readableBytes() < 4) {
          in.resetReaderIndex()
          return
        }

        out.add(ConnectToAssetService(ClientVersion(in.readInt())))

      case otherwise =>
        throw new Exception(s"unexpected service connect directive of value $otherwise")
    }
  }
}
