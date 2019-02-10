package io.versailles.application.event

import io.netty.buffer.ByteBuf
import io.versailles.application.model.{Archive, Folder}

/**
  * An event that denotes a [[Folder]] of assets has been ejected
  * and is therefore ready for transport.
  * @author Sino
  */
case class AssetFolderEjected(
  archive: Archive,
  folder: Folder,
  blocks: Seq[ByteBuf]
)
