package io.unity.application.component.middleware.encoding

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder
import io.unity.application.event.AssetFolderEjected

/**
  * A [[MessageToByteEncoder]] that translates the given [[AssetFolderEjected]]
  * event to bytes to write to an outgoing stream.
  * @author Sino
  */
final class AssetFolderEjectionEncoder extends MessageToByteEncoder[AssetFolderEjected] {
  val BlockSize = 512
  val EndOfBlockFlag = 255

  override def encode(ctx: ChannelHandlerContext, msg: AssetFolderEjected, out: ByteBuf) = {
    out.writeByte(msg.archive.toValue)
    out.writeShort(msg.folder.toValue)
    out.writeByte(0) // TODO

    val totalTrailSize = msg.blocks.map(_.readableBytes()).sum
    out.writeInt(totalTrailSize)

    msg.blocks.foreach { dataBlock =>
      val requireEndOfBlockFlag = dataBlock.readableBytes() >= BlockSize

      out.writeBytes(dataBlock)
      if (requireEndOfBlockFlag) {
        out.writeByte(EndOfBlockFlag)
      }
    }
  }
}
