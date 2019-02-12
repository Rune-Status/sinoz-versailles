package io.versailles.application.event

import io.netty.buffer.ByteBuf
import io.versailles.application.model.{ArchiveId, FolderId}

/**
  * An event that denotes a folder of assets has been ejected
  * and is therefore ready for transport.
  * @author Sino
  */
case class AssetFolderEjected(
  archive: ArchiveId,
  folder: FolderId,
  blocks: Seq[ByteBuf]
)
