package io.versailles.application.launch

import java.nio.file.{Path, Paths}

import akka.actor.ActorSystem
import io.versailles.application.storage.{FileBundle, Storage}
import redis.RedisClient

/**
  * The main entry point to this game server application.
  * @author Sino
  */
object VersaillesLauncher {
  /** The [[Path]] to the main Versailles configuration file. */
  val pathToVersaillesConfig = Paths.get("resources/configs/versailles.conf")

  def main(args: Array[String]): Unit = {
    println("Hello World")
  }

  /** Attempts to load a [[VersaillesConfig]] from a local configuration file
    * on disk. */
  private def loadVersaillesConfig(path: Path) =
    VersaillesConfig.load(path)

  /** Attempts to load the asset binary storage into memory. */
  private def loadAssetStorage(path: Path) =
    new Storage(FileBundle.open(path.toFile))

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
