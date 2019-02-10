package io.versailles.application.model

/**
  * A request for a particular kind of [[Folder]] stored within an [[Archive]].
  * @author Sino
  */
case class FolderRequest(
  archive: Archive,
  folder: Folder
)
