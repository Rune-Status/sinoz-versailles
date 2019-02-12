package io.versailles.application.component.middleware.handler

import akka.actor.{Actor, ActorRef, Props}
import com.twitter.util.StorageUnit
import io.versailles.application.command.{ConnectToAssetService, ConnectToLoginService}
import io.versailles.application.component.asset.FolderConveyor
import io.versailles.application.component.middleware.Channel._
import io.versailles.application.component.middleware.encoding.{AssetFolderEjectionEncoder, AssetFolderRequestDecoder, LoginRequestDecoder, LoginResponseEncoder}
import io.versailles.application.event.ServiceResponded
import io.versailles.application.event.ServiceResponded.MayProceed
import io.versailles.application.model.{ClientVersion, CredentialBlockKeySet, Nonce}

object ServiceConnectHandler {
  def props(channel: ActorRef, versionExpected: ClientVersion, archiveCount: Int, credentialsBlockKeySet: CredentialBlockKeySet, folderBlockSize: StorageUnit, folderConveyor: FolderConveyor) =
    Props(new ServiceConnectHandler(channel, versionExpected, archiveCount, credentialsBlockKeySet, folderBlockSize, folderConveyor))
}

/**
  * The application logic handler to deal with initial service handshake messages.
  * @author Sino
  */
final class ServiceConnectHandler(channel: ActorRef, versionExpected: ClientVersion, archiveCount: Int, credentialsBlockKeySet: CredentialBlockKeySet, blockEjectionLimit: StorageUnit, folderConveyor: FolderConveyor) extends Actor {
  override def receive = {
    case ConnectToAssetService =>
      // TODO

    case ConnectToLoginService =>
      // TODO
  }

  private def acceptLoginServiceConnect(nonce: Nonce): Unit = {
    channel ! SetHandler(null)

    channel ! Send(MayProceed(Some(nonce)))
    channel ! Flush

    channel ! SetDecoder(new LoginRequestDecoder(archiveCount, credentialsBlockKeySet))
    channel ! SetEncoder(new LoginResponseEncoder())
  }

  private def acceptAssetServiceConnect(): Unit = {
    channel ! SetHandler(AssetFolderStreamingHandler.props(channel, folderConveyor, blockEjectionLimit))

    channel ! Send(MayProceed(None))
    channel ! Flush

    channel ! SetDecoder(new AssetFolderRequestDecoder)
    channel ! SetEncoder(new AssetFolderEjectionEncoder(blockEjectionLimit))
  }

  private def reject(reason: ServiceResponded.Type): Unit = {
    channel ! Send(reason)
    channel ! Flush
  }
}
