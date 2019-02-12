package io.versailles.application.component.middleware.handler

import akka.actor.{Actor, ActorRef, Props, Timers}
import com.twitter.util.StorageUnit
import io.versailles.application.command.{AcknowledgeClientStateChange, RequestAssetFolder}
import io.versailles.application.component.asset.FolderConveyor
import io.versailles.application.component.middleware.Channel.{Flush, Send}
import io.versailles.application.event.AssetFolderEjected
import io.versailles.application.model.AssetFolder

import scala.concurrent.duration._

object AssetFolderStreamingHandler {
  def props(channel: ActorRef, folderConveyor: FolderConveyor, blockEjectionLimit: StorageUnit) =
    Props(new AssetFolderStreamingHandler(channel, folderConveyor, blockEjectionLimit))
}

/**
  * The application logic handler that processes requests from the client
  * to stream particular folder of assets.
  *
  * @author Sino
  */
final class AssetFolderStreamingHandler(channel: ActorRef, conveyor: FolderConveyor, defaultBlockEjectionLimit: StorageUnit) extends Actor with Timers {
  /** The current amount of blocks to limit the streaming to. This limit may
    * vary over time (such as when a player is logged out, the limit may be
    * decreased and increased when logged back in). */
  var blockEjectionLimit = defaultBlockEjectionLimit

  override def preStart(): Unit = {
    // schedules the streaming job to run once a second
    timers.startPeriodicTimer(StreamJob, Stream, 1.second)
  }

  override def postStop(): Unit = {
    timers.cancel(StreamJob)
  }

  override def receive = {
    case RequestAssetFolder(archive, folder, isUrgent) =>
      conveyor.convey(archive, folder, isUrgent)

    case AcknowledgeClientStateChange(newClientState) =>
      // TODO

    case Stream =>
      val ejectableFolders = conveyor.update(blockEjectionLimit)

      ejectableFolders.foreach(folder => eject(folder))
      if (ejectableFolders.nonEmpty) {
        endOfBatch()
      }
  }

  /** Ejects the given [[AssetFolder]] for transport. */
  def eject(subject: AssetFolder): Unit = {
    channel ! Send(AssetFolderEjected(subject.archive, subject.folder, subject.blocks))
  }

  /** Marks it as the end of a stream batch by calling a channel flush. */
  def endOfBatch(): Unit = {
    channel ! Flush
  }

  case object Stream
  case object StreamJob
}
