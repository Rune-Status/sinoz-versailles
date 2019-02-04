package io.unity.application.model

import io.netty.buffer.ByteBuf

/**
  * A serve of a particular [[Folder]] to the client.
  * @author Sino
  */
case class FolderServe(
  archive: Archive,
  folder: Folder,
  blocks: Seq[ByteBuf]
)
