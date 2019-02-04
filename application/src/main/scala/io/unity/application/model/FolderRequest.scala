package io.unity.application.model

/**
  * An event that denotes that the client is requesting a folder to be
  * served from the server.
  * @author Sino
  */
case class FolderRequest(
  archive: Archive,
  folder: Folder,
  urgent: Boolean
)
