package io.versailles.application.launch

import java.net.InetSocketAddress
import java.nio.file.{Path, Paths}

import akka.actor.ActorSystem
import io.versailles.application.config.ApplicationConfig
import redis.RedisClient

/**
  * Launches this game server application.
  * @author Sino
  */
object VersaillesLauncher {
  /** The [[Path]] to the main application configuration file. */
  private val pathToAppConfig = Paths.get("resources/configs/versailles.conf")

  /** The main entry point. */
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("VersaillesSystem")

    val config = loadVersaillesConfig(pathToAppConfig)

    val redisClient = createRedisClient(getInMemoryStoreHost, getInMemoryStorePort)(system)

    val serverPort = new InetSocketAddress(getServerPort)

    // spawns the aggregate root that has ownership of all of the components
    system.actorOf(VersaillesApplication.props(config, redisClient, serverPort), name = "versailles")
  }

  /** Attempts to load a [[ApplicationConfig]] from a local configuration file
    * on disk. */
  private def loadVersaillesConfig(path: Path) =
    ApplicationConfig.load(path)

  /** Produces a new [[RedisClient]] which is to automatically connect to a
    * local or remote Redis server using the specified details. */
  private def createRedisClient(host: String, port: Int)(implicit system: ActorSystem) =
    RedisClient(host, port)

  /** Retrieves the host of the in-memory store from the environment variables.
    * If the environment variable isn't set, a default is returned. */
  private def getInMemoryStoreHost =
    env("VERSAILLES_MEMSTORE_HOST").getOrElse("localhost")

  /** Retrieves the port of the in-memory store from the environment variables.
    * If the environment variable isn't set, a default is returned. */
  private def getInMemoryStorePort =
    env("VERSAILLES_MEMSTORE_PORT").getOrElse("6379").toInt

  /** Retrieves the port of the middleware server from the environment
    * variables. If the environment variable isn't set, a default is
    * returned. */
  private def getServerPort =
    env("VERSAILLES_PORT").getOrElse("43594").toInt

  /** Looks up an environment variable. */
  private def env(name: String): Option[String] =
    Option(System.getenv(name))
}
