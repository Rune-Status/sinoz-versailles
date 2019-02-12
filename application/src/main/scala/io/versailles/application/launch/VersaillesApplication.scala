package io.versailles.application.launch

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import io.versailles.application.component.asset.{FolderConveyor, FolderPagesCache}
import io.versailles.application.component.middleware.Server.BindTo
import io.versailles.application.component.middleware.encoding.{ServiceConnectDecoder, ServiceResponseEncoder}
import io.versailles.application.component.middleware.handler.ServiceConnectHandler
import io.versailles.application.component.middleware.{ChannelSupervisor, Server}
import io.versailles.application.config.ApplicationConfig
import io.versailles.application.storage.{FileBundle, Storage}
import redis.RedisClient

object VersaillesApplication {
  def props(config: ApplicationConfig, redisClient: RedisClient, serverAddress: InetSocketAddress) =
    Props(new VersaillesApplication(config, redisClient, serverAddress))
}

/**
  * The aggregate root actor of this application that has ownership of
  * all of the components that make up this game server.
  * @author Sino
  */
final class VersaillesApplication(config: ApplicationConfig, redisClient: RedisClient, serverAddress: InetSocketAddress) extends Actor {
  /** The OldSchool RuneScape storage of assets. */
  val assetStorage = new Storage(FileBundle.open(config.storagePath.toFile))

  /** The cache of folder pages and the corresponding conveyor of assets of folders. */
  val folderPagesCache = new FolderPagesCache(assetStorage)
  val folderConveyor = new FolderConveyor(folderPagesCache)

  /** Supervises over the connected client channels. */
  val channelSupervisor = context.actorOf(ChannelSupervisor.props(produceInitialMessageHandler, produceInitialDecoder, produceInitialEncoder), name = "channels")

  /** The middleware server that is exposed to clients to communicate with. */
  val server = context.actorOf(Server.props(channelSupervisor), name = "middleware")

  override def preStart(): Unit = {
    startMiddlewareServer()
  }

  /** Tells the [[server]] actor to start listening for incoming connections. */
  def startMiddlewareServer(): Unit = {
    server ! BindTo(serverAddress)
  }

  /** Produces a type of middleware handler to set as the default when a
    * new channel actor is created. */
  def produceInitialMessageHandler(channel: ActorRef) =
    ServiceConnectHandler.props(channel, config.clientVersion, assetStorage.getArchiveCount, config.credentialsKeySet, config.assetsServeLimit, folderConveyor)

  /** Produces the initial codecs to set the default when a new channel
    * actor is created. */
  def produceInitialDecoder() = new ServiceConnectDecoder
  def produceInitialEncoder() = new ServiceResponseEncoder

  override def receive = {
    case msg =>
      throw new IllegalArgumentException
  }
}
