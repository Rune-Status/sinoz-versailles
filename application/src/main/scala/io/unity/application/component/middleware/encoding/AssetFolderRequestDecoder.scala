package io.unity.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.unity.application.command.{AcknowledgeClientStateChange, ApplyEncryptionToFolder, RequestAssetFolder}
import io.unity.application.model.{Archive, ClientState, Folder}

/**
  * A [[ByteToMessageDecoder]] that translates a stream of bytes to either a:
  *
  * - [[RequestAssetFolder]]
  * - [[AcknowledgeClientStateChange]]
  * - [[ApplyEncryptionToFolder]]
  *
  * message depending on the initial directive that is given.
  * @author Sino
  */
final class AssetFolderRequestDecoder extends ByteToMessageDecoder {
  override def decode(ctx: ChannelHandlerContext, in: ByteBuf, out: java.util.List[AnyRef]) = {
    val directive = in.readUnsignedByte()

    directive match {
      case 0 | 1 =>
        val archive = Archive(in.readUnsignedByte())
        val folder = Folder(in.readUnsignedShort())
        val urgent = directive == 1

        out.add(RequestAssetFolder(archive, folder, urgent))

      case 2 =>
        out.add(AcknowledgeClientStateChange(ClientState.LoggingIn))
        in.skipBytes(3)

      case 3 =>
        out.add(AcknowledgeClientStateChange(ClientState.LoggedOut))
        in.skipBytes(3)

      case 4 =>
        val mask = in.readUnsignedByte()
        in.skipBytes(2)
        out.add(ApplyEncryptionToFolder(mask))

      case otherwise =>
        throw new Exception(s"unexpected directive of value $directive")
    }
  }
}
