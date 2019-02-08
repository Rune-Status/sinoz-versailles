package io.unity.application.component.middleware.handler

import com.twitter.util.StorageUnit
import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import io.netty.util.concurrent.ScheduledFuture
import io.unity.application.command.{AcknowledgeClientStateChange, ApplyEncryptionToFolder, RequestAssetFolder}
import io.unity.application.event.AssetFolderEjected
import io.unity.application.model.{Archive, Folder, FolderRequest}
import io.unity.application.storage.Storage

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.duration._

/**
  * The application logic handler that processes requests from the client
  * to stream particular folder of assets.
  *
  * This implementation runs a job at an interval rate of 1 second. Requests
  * that arrive are at first hand put into a queue depending on their urgency.
  * The job entails polling from these queues of requests and preparing them
  * for transport. Once prepared, they are ejected for message encoding before
  * being sent off to the game client.
  *
  * The job only polls a limited amount of [[FolderRequest]]s. This is mainly
  * for bandwidth control. Folders are during preparation partitioned into
  * blocks of 512 bytes. This allows accumulation of a limited amount of
  * data (which are to be ejected) and delaying the remaining amount for the
  * next batch.
  *
  * @author Sino
  */
// TODO cache folder pages to avoid massive amounts of duplicate memory being allocated
final class AssetStreamingHandler(assetStorage: Storage, dataLimit: StorageUnit) extends ChannelInboundHandlerAdapter {
  /** The rate at which requests for asset folders are polled and served. */
  private val Rate = 1.second

  /** The size of a data block of an asset folder. */
  private val BlockSize = 512

  /** The scheduled task that runs the poll job at an interval rate. */
  private var pollTask: Option[ScheduledFuture[_]] = None

  /** The queues of [[FolderRequest]]s. */
  private val urgent = mutable.Queue[FolderRequest]()
  private val regular = mutable.Queue[FolderRequest]()

  /** The amount of blocks that have been ejected for transport. */
  private var blockEjectionCount = 0

  /** Schedules the [[pollTask]] to run at an interval [[Rate]]. */
  private def schedulePollTask(ctx: ChannelHandlerContext): Unit = {
    pollTask = Some(ctx.channel().eventLoop().scheduleAtFixedRate(() => pollAll(ctx), 0, Rate.length, Rate.unit))
  }

  /** Cancels a running [[pollTask]]. */
  private def cancelPollTask(): Unit = {
    pollTask.foreach(_.cancel(false))
    pollTask = None
  }

  /** Polls from both the [[urgent]] and [[regular]] queue of
    * [[FolderRequest]]s. */
  private def pollAll(ctx: ChannelHandlerContext): Unit = {
    try {
      poll(ctx, urgent)
      poll(ctx, regular)
    } finally {
      resetBlockEjectionCount()
      endOfBatch(ctx)
    }
  }

  /** Polls from the given queue of [[FolderRequest]]s to select which ones
    * are to be ejected for transport. */
  private def poll(ctx: ChannelHandlerContext, queue: mutable.Queue[FolderRequest]): Unit = {
    while (queue.nonEmpty && !exceededBlockEjectionLimit) {
      val request = queue.dequeue()

      val folderPages = getFolderOrManifest(request.archive, request.folder)
      val partitions = partition(Unpooled.wrappedBuffer(folderPages), BlockSize)

      eject(ctx, request, partitions)
    }
  }

  /** Partitions the given [[ByteBuf]] into partitions of the specified
    * amount of bytes. */
  @tailrec
  private def partition(in: ByteBuf, partitionSize: Int, blocks: Seq[ByteBuf] = Seq.empty[ByteBuf]): Seq[ByteBuf] = {
    if (!in.isReadable || partitionSize <= 0) {
      blocks
    } else {
      val block = safeSlice(in, partitionSize)
      partition(in, partitionSize, blocks :+ block)
    }
  }

  /** Safely slices away the specified amount of bytes from the given
    * [[ByteBuf]]. If the given size is larger than what's available,
    * the remains are taken instead. */
  private def safeSlice(in: ByteBuf, size: Int) = {
    if (in.readableBytes() < size) {
      in.readSlice(in.readableBytes())
    } else {
      in.readSlice(size)
    }
  }

  /** Fetches either an actual folder with assets or the release manifest,
    * depending on the specified [[Archive]] and [[Folder]]. */
  private def getFolderOrManifest(archive: Archive, folder: Folder) = {
    if (refersToReleaseManifest(archive, folder)) {
      getReleaseManifest
    } else {
      getFolder(archive, folder)
    }
  }

  /** Looks up the specified [[Folder]] within the specified [[Archive]]. */
  private def getFolder(archive: Archive, folder: Folder) =
    assetStorage
      .getBundle
      .readPages(archive.toValue, folder.toValue)

  /** Generates a new release manifest. */
  private def getReleaseManifest =
    assetStorage
      .createReleaseManifest()
      .encode()

  /** Returns whether the given pair of [[Archive]] and [[Folder]] are
    * referring to the release manifest. */
  private def refersToReleaseManifest(archive: Archive, folder: Folder) =
    archive.toValue == 255 && folder.toValue == 255

  /** Ejects the given [[FolderRequest]] with the given sequence of blocks
    * for transport. */
  private def eject(ctx: ChannelHandlerContext, request: FolderRequest, blocks: Seq[ByteBuf]): Unit = {
    ctx.write(AssetFolderEjected(request.archive, request.folder, blocks))

    incrementBlockEjectionCount(blocks.size)
  }

  /** Marks the end of a batch, flushing all of the ejected asset folders. */
  private def endOfBatch(ctx: ChannelHandlerContext): Unit = {
    ctx.flush()
  }

  /** Checks if the [[blockEjectionCount]] has exceeded the [[dataLimit]]
    * in bytes. */
  private def exceededBlockEjectionLimit = {
    val dataLimitInBytes = dataLimit.bytes
    val dataLimitInBlocks = dataLimitInBytes / BlockSize

    blockEjectionCount >= dataLimitInBlocks
  }

  /** Increments the [[blockEjectionCount]] by the specified amount. */
  private def incrementBlockEjectionCount(amount: Int): Unit = {
    blockEjectionCount += amount
  }

  /** Resets the [[blockEjectionCount]] to zero. */
  private def resetBlockEjectionCount(): Unit = {
    blockEjectionCount = 0
  }

  /** Conveys the given [[FolderRequest]] onto an appropriate
    * queue of [[FolderRequest]]s. */
  private def convey(request: FolderRequest, isUrgent: Boolean): Unit = {
    if (isUrgent) {
      urgent.enqueue(request)
    } else {
      regular.enqueue(request)
    }
  }

  override def handlerAdded(ctx: ChannelHandlerContext): Unit = {
    schedulePollTask(ctx)
  }

  override def handlerRemoved(ctx: ChannelHandlerContext): Unit = {
    cancelPollTask()
  }

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    msg match {
      case RequestAssetFolder(archive, folder, isUrgent) =>
        convey(FolderRequest(archive, folder), isUrgent)

      case AcknowledgeClientStateChange(newState) =>
        // TODO

      case ApplyEncryptionToFolder(mask) =>
        // TODO
    }
  }
}
