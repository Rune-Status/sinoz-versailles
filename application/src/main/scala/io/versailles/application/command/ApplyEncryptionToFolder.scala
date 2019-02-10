package io.versailles.application.command

/**
  * A client's command to apply encryption to a
  * [[io.versailles.application.model.Folder]] before it is served.
  * @author Sino
  */
case class ApplyEncryptionToFolder(mask: Int)
