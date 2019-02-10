package io.versailles.application.command

import io.versailles.application.model.{Archive, Folder}

/**
  * A client's command to request the server for a particular
  * type of [[Folder]] stored within an [[Archive]].
  * @author Sino
  */
case class RequestAssetFolder(
  archive: Archive,
  folder: Folder,
  urgent: Boolean
)
