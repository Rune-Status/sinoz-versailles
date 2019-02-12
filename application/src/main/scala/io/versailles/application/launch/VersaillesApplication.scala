package io.versailles.application.launch

import java.net.InetSocketAddress

import akka.actor.{Actor, ActorRef, Props}
import io.versailles.application.component.account.{AccountService, PostgresAccountRepository}
import io.versailles.application.component.asset.{FolderConveyor, FolderPagesCache}
import io.versailles.application.component.auth.{AuthenticationService, BCryptPasswordMatcher}
import io.versailles.application.component.character.{CharacterService, PostgresCharacterRepository, RedisCharacterCache}
import io.versailles.application.component.game.GameService
import io.versailles.application.component.login.LoginService
import io.versailles.application.component.middleware.Server.BindTo
import io.versailles.application.component.middleware.encoding.{ServiceConnectDecoder, ServiceResponseEncoder}
import io.versailles.application.component.middleware.handler.ServiceConnectHandler
import io.versailles.application.component.middleware.{ChannelSupervisor, Server}
import io.versailles.application.config.ApplicationConfig
import io.versailles.application.storage.{FileBundle, Storage}
import redis.RedisClient

import scala.concurrent.duration._

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

  /** The cache of folder pages and the corresponding conveyor of assets of
    * folders. */
  val folderPagesCache = new FolderPagesCache(assetStorage)
  val folderConveyor = new FolderConveyor(folderPagesCache)

  /** The service that deals with user accounts and the repository associated
    * with it. */
  val accountRepo = new PostgresAccountRepository
  val accountService = new AccountService(accountRepo)

  /** The service that deals with character profile and the repository and
    * cache associated with it. */
  val characterRepo = new PostgresCharacterRepository
  val characterCache = new RedisCharacterCache(redisClient)
  val characterService = new CharacterService(characterRepo, characterCache, expireAfter = 10.minutes) // TODO load expiration time from config

  /** The service that deals with authenticating user accounts and the
    * password matcher to use during authentication. */
  val passwordMatcher = new BCryptPasswordMatcher
  val authenticationService = new AuthenticationService(accountService, passwordMatcher)

  /** The game service that provides gameplay to users that are logged in. */
  val gameService = context.actorOf(GameService.props, name = "game")

  /** The login service that logs users into the game world. */
  val loginService = new LoginService(authenticationService, characterService, gameService)

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
