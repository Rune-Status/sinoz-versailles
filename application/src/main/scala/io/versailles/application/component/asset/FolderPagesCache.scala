package io.versailles.application.component.asset

import java.nio.ByteBuffer

import io.versailles.application.model.{ArchiveId, FolderId}
import io.versailles.application.storage.Storage

import scala.collection.concurrent.TrieMap

/**
  * A cache of asset folder pages.
  * @author Sino
  */
final class FolderPagesCache(storage: Storage) {
  type Id = Int
  type Pages = ByteBuffer

  /** A collection of pages that together represent a folder. */
  // TODO is triemap the correct data structure? perhaps use ConcurrentHashMap instead?
  private val folders = TrieMap[Id, Pages]()

  /** Looks up a folder of pages. If it doesn't exist, it is fetched
    * from the [[storage]] and then stored into the cache. Note that
    * a duplicate will always be returned so it is safe to consume
    * from the given [[ByteBuffer]]. */
  def get(archive: ArchiveId, folder: FolderId) =
    folders
      .getOrElseUpdate(toId(archive, folder), fetch(archive, folder))
      .duplicate()

  /** Fetches the pages of the specified folder stored within the specified
    * archive. */
  private def fetch(archive: ArchiveId, folder: FolderId) =
    if (refersToReleaseManifest(archive, folder)) {
      storage.createReleaseManifest().encode()
    } else {
      storage.getBundle.readPages(archive.toValue, folder.toValue)
    }

  /** Checks if the given [[ArchiveId]] and [[FolderId]] pair reference the
    * release manifest within the asset storage. */
  private def refersToReleaseManifest(archive: ArchiveId, folder: FolderId) =
    archive.toValue == 255 && folder.toValue == 255

  /** Returns a bitpack value of the given parameters. */
  private def toId(archive: ArchiveId, folder: FolderId) =
    archive.toValue << 16 | folder.toValue
}
