package io.versailles.application.command

import io.versailles.application.model.{ArchiveId, FolderId}

/**
  * A command to request the server for a particular
  * type of folder stored within an archive.
  * @author Sino
  */
case class RequestAssetFolder(
  archive: ArchiveId,
  folder: FolderId,
  urgent: Boolean
)
