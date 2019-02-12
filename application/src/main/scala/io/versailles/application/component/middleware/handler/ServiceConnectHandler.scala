package io.versailles.application.component.middleware.handler

import akka.actor.{Actor, ActorRef, Props}
import com.twitter.util.StorageUnit
import io.versailles.application.command.{ConnectToAssetService, ConnectToLoginService}
import io.versailles.application.component.asset.FolderConveyor
import io.versailles.application.component.middleware.Channel._
import io.versailles.application.component.middleware.encoding._
import io.versailles.application.event.ServiceResponded
import io.versailles.application.event.ServiceResponded.{ClientOutOfDate, MayProceed}
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
    case ConnectToAssetService(receivedVersion) =>
      if (receivedVersion.isUpToDateWith(versionExpected)) {
        acceptAssetServiceConnect()
      } else {
        reject(reason = ClientOutOfDate)
      }

    case ConnectToLoginService =>
      acceptLoginServiceConnect(Nonce.generate)
  }

  private def acceptLoginServiceConnect(nonce: Nonce): Unit = {
    channel ! SetHandler(LoginRequestHandler.props(nonce))

    respondWith(MayProceed(Some(nonce)))

    channel ! SetDecoder(new LoginRequestDecoder(archiveCount, credentialsBlockKeySet))
    channel ! SetEncoder(new LoginResponseEncoder())

    cleanup()
  }

  private def acceptAssetServiceConnect(): Unit = {
    channel ! SetHandler(AssetFolderStreamingHandler.props(channel, folderConveyor, blockEjectionLimit))

    respondWith(MayProceed(nonce = None))

    channel ! SetDecoder(new AssetFolderRequestDecoder)
    channel ! SetEncoder(new AssetFolderEjectionEncoder)

    cleanup()
  }

  private def reject(reason: ServiceResponded.Type): Unit = {
    respondWith(reason)

    channel ! Terminate
    context stop self
  }

  private def respondWith(message: Any): Unit = {
    channel ! Send(message)
    channel ! Flush
  }

  /** Cleans this actor up by putting it to a stop so it can be garbage collected. */
  private def cleanup(): Unit = {
    context stop self
  }
}
