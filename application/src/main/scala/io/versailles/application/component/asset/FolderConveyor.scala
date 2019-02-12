package io.versailles.application.component.asset

import com.twitter.conversions.StorageUnitOps._
import com.twitter.util.StorageUnit
import io.netty.buffer.{ByteBuf, Unpooled}
import io.versailles.application.component.middleware.encoding.buffer._
import io.versailles.application.model.{ArchiveId, AssetFolder, FolderId}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.collection.immutable

object FolderConveyor {
  /** The size of a single ejectable block. */
  val BlockSize = 512.bytes
}

/**
  * A conveyor of [[AssetFolder]]s that are to be ejected for transport.
  *
  * This implementation serves [[AssetFolder]]s in batches by taking
  * control of how many blocks of [[AssetFolder]]s should be served at a
  * specific point in time. Folders are partitioned into blocks of
  * 512 bytes. Because this implementation is dealing with fixed sized
  * data blocks, it allows it to accumulate an exact amount of data
  * for ejection and thus have control over network bandwidth. When
  * the conveyor notices that it is reaching its limit, it is delaying
  * the remaining blocks for the next batch.
  *
  * @author Sino
  */
final class FolderConveyor(foldersCache: FolderPagesCache) {
  import FolderConveyor._

  /** The queues of asset folders that await ejection. */
  private val urgent = mutable.Queue[AssetFolder]()
  private val regular = mutable.Queue[AssetFolder]()

  /** The amount of blocks that have been ejected thus far. */
  private var blockEjectionCount = 0

  /** Updates this conveyor, returning a collection of [[AssetFolder]]s
    * that are ready for transport. */
  def update(implicit blockEjectionLimit: StorageUnit) = {
    val toEject = poll(urgent) ++ poll(regular)
    resetBlockEjectionCount()
    toEject
  }

  /** Looks up a folder stored within the specified archive and conveys
    * it for transport. */
  def convey(archiveId: ArchiveId, folderId: FolderId, isUrgent: Boolean): Unit = {
    val pages = getFolderPages(archiveId, folderId)
    val blocks = partition(Unpooled.wrappedBuffer(pages), BlockSize)

    if (isUrgent) {
      urgent.enqueue(AssetFolder(archiveId, folderId, blocks))
    } else {
      regular.enqueue(AssetFolder(archiveId, folderId, blocks))
    }
  }

  /** Polls from the given queue of [[AssetFolder]] to eject them. */
  private def poll(queue: mutable.Queue[AssetFolder])(implicit blockEjectionLimit: StorageUnit) = {
    var readyForTransport = immutable.Seq.empty[AssetFolder]

    while (queue.nonEmpty && !exceededBlockEjectionLimit(blockEjectionLimit)) {
      val folder = queue.dequeue()
      // TODO take N blocks from folder to eject to avoid exceeding the limit for bandwidth control, delay the rest

      readyForTransport :+= folder
      incrementBlockEjectionCount(folder.blocks.size)
    }

    readyForTransport
  }

  /** Increments the [[blockEjectionCount]] by the given amount to properly
    * keep track of how many blocks have been ejected for transport. */
  private def incrementBlockEjectionCount(amount: Int): Unit = {
    blockEjectionCount += amount
  }

  /** Resets the [[blockEjectionCount]] back to zero. */
  private def resetBlockEjectionCount(): Unit = {
    blockEjectionCount = 0
  }

  /** Checks if the [[blockEjectionCount]] has exceeded the
    * `[blockEjectionLimit` in bytes. */
  private def exceededBlockEjectionLimit(blockEjectionLimit: StorageUnit) = {
    val dataLimitInBytes = blockEjectionLimit.bytes
    val dataLimitInBlocks = dataLimitInBytes / BlockSize.bytes

    blockEjectionCount >= dataLimitInBlocks
  }

  /** Partitions the given [[ByteBuf]] into partitions of fixed sized blocks.
    * The size of each block is determined by the given `partitionSize`
    * parameter. */
  @tailrec
  private def partition(in: ByteBuf, partitionSize: StorageUnit, blocks: Seq[ByteBuf] = Seq.empty[ByteBuf]): Seq[ByteBuf] = {
    if (!in.isReadable || partitionSize <= 0.bytes) {
      blocks
    } else {
      val block = in.readSafeSlice(partitionSize)
      partition(in, partitionSize, blocks :+ block)
    }
  }

  /** Fetches the requested collection of pages that together make up the folder. */
  private def getFolderPages(archive: ArchiveId, folder: FolderId) =
    foldersCache.get(archive, folder)
}
