package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.versailles.application.command.{AcknowledgeClientStateChange, RequestAssetFolder}
import io.versailles.application.component.middleware.MessageDecoder
import io.versailles.application.component.middleware.MessageDecoder.Result.{MessageOutput, RequireMoreBytes}
import io.versailles.application.model.{ArchiveId, ClientState, FolderId}

/**
  * Decodes requests for a folder of assets.
  * @author Sino
  */
final class AssetFolderRequestDecoder extends MessageDecoder {
  override def decode(in: ByteBuf): MessageDecoder.Result.Type = {
    if (in.readableBytes() < 4) {
      return RequireMoreBytes
    }

    val directive = in.readUnsignedByte()

    directive match {
      case 0 | 1 =>
        val archive = ArchiveId(in.readUnsignedByte())
        val folder = FolderId(in.readUnsignedByte())
        val urgent = directive == 1

        MessageOutput(RequestAssetFolder(archive, folder, urgent))

      case 2 | 3 =>
        in.skipBytes(3)

        if (directive == 2) {
          MessageOutput(AcknowledgeClientStateChange(ClientState.LoggedIn))
        } else {
          MessageOutput(AcknowledgeClientStateChange(ClientState.LoggedOut))
        }

      case otherwise =>
        throw new Exception(s"unexpected directive of value $otherwise")
    }
  }
}
