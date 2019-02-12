package io.versailles.application.component.middleware.encoding

import com.twitter.util.StorageUnit
import io.netty.buffer.ByteBuf
import io.versailles.application.component.middleware.MessageEncoder
import io.versailles.application.event.AssetFolderEjected

/** @author Sino */
final class AssetFolderEjectionEncoder(blockSize: StorageUnit) extends MessageEncoder[AssetFolderEjected] {
  val EndOfBlockFlag = 255

  override def encode(message: AssetFolderEjected, out: ByteBuf): Unit = {
    out.writeByte(message.archive.toValue)
    out.writeShort(message.archive.toValue)
    out.writeByte(0)

    val totalTrailSize = message.blocks.map(_.readableBytes()).sum
    out.writeInt(totalTrailSize)

    message.blocks.foreach { block =>
      out.writeBytes(block)

      if (block.readableBytes() >= blockSize.bytes) {
        out.writeByte(EndOfBlockFlag)
      }
    }
  }
}
