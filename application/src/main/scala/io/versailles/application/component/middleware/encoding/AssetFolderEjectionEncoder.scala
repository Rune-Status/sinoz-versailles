package io.versailles.application.component.middleware.encoding

import com.twitter.conversions.StorageUnitOps._
import io.netty.buffer.ByteBuf
import io.versailles.application.component.middleware.MessageEncoder
import io.versailles.application.event.AssetFolderEjected

object AssetFolderEjectionEncoder {
  /** The size of a single ejectable block. */
  val BlockSize = 512.bytes

  /** A value that marks the end of a complete data block. */
  val EndOfBlockFlag = 255
}

/** @author Sino */
final class AssetFolderEjectionEncoder extends MessageEncoder[AssetFolderEjected] {
  import AssetFolderEjectionEncoder._

  override def encode(message: AssetFolderEjected, out: ByteBuf): Unit = {
    out.writeByte(message.archive.toValue)
    out.writeShort(message.archive.toValue)
    out.writeByte(0)

    val totalTrailSize = message.blocks.map(_.readableBytes()).sum
    out.writeInt(totalTrailSize)

    message.blocks.foreach { block =>
      out.writeBytes(block)

      if (block.readableBytes() >= BlockSize.bytes) {
        out.writeByte(EndOfBlockFlag)
      }
    }
  }
}
