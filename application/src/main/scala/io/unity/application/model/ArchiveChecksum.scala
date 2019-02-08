package io.unity.application.model

/**
  * A CRC32 checksum of an archive.
  * @author Sino
  */
case class ArchiveChecksum(private val value: Int) extends AnyVal {
  def toValue = value
}
