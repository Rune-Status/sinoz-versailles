package io.versailles.application.model

import io.netty.buffer.ByteBuf

/**
  * A folder of assets.
  * @author Sino
  */
case class AssetFolder(
  archive: ArchiveId,
  folder: FolderId,
  blocks: Seq[ByteBuf]
)
