package io.unity.application.launch

import java.nio.file.{Path, Paths}

import com.redis.RedisClient
import io.unity.application.storage.{Cache, FileStore}
import org.slf4j.LoggerFactory
import scalaz.zio.duration._
import scalaz.zio.{App, IO, system}

/**
  * The main entry point to this game server application.
  * @author Sino
  */
object UnityLauncher extends App {
  /** The [[Path]] to the main Unity configuration file. */
  val pathToUnityConfig = Paths.get("resources/configs/unity.conf")

  /** Logs information and errors to either console or file. */
  val logger = LoggerFactory.getLogger(UnityLauncher.getClass)

  override def run(args: List[String]) =
    launch
      .attempt
      .map(_.fold(failure => exitWhenDone, success => exitNow))

  /** Returns the exit status for delayed system exit. */
  private def exitWhenDone = ExitStatus.ExitWhenDone(code = 1, timeout = 1.minute)

  /** Returns the exit status for immediate system exit. */
  private def exitNow = ExitStatus.ExitNow(code = 0)

  /** Sets up the application components, loads resources into memory and boots
    * up the server to deal with requests. */
  private def launch: IO[Exception, Unit] =
    for {
      _             <- info("Starting Unity...")

      config        <- loadUnityConfig(pathToUnityConfig)
      _             <- info(s"Expected client version: ${config.clientVersion}")

      cache         <- loadAssetStorage(config.storagePath)
      _             <- info(s"Loaded asset storage that's located at ${config.storagePath}")

      memStoreHost  <- getInMemoryStoreHost
      memStorePort  <- getInMemoryStorePort

      redisClient   <- connectToRedisServer(memStoreHost, memStorePort)
      _             <- info(s"Connected to in-memory store at $memStoreHost:$memStorePort")
    } yield ()

  /** Attempts to load a [[UnityConfig]] from a local configuration file
    * on disk. */
  private def loadUnityConfig(path: Path) =
    UnityConfig.load(path)

  /** Attempts to synchronously load the asset binary storage into memory. */
  private def loadAssetStorage(path: Path) =
    IO.sync(new Cache(FileStore.open(path.toFile)))

  /** Attempts to synchronously connect to a local or remote Redis server using
    * the specified details. */
  private def connectToRedisServer(host: String, port: Int) =
    IO.sync(new RedisClient(host, port))

  /** Retrieves the host of the in-memory store from the environment variables.
    * If the environment variable isn't set, a default is returned. */
  private def getInMemoryStoreHost =
    system.env("UNITY_MEMSTORE_HOST").map(_.getOrElse("localhost"))

  /** Retrieves the port of the in-memory store from the environment variables.
    * If the environment variable isn't set, a default is returned. */
  private def getInMemoryStorePort =
    system
      .env("UNITY_MEMSTORE_PORT")
      .map(_.getOrElse("6379"))
      .map(_.toInt)

  /** Logs the given info message. */
  private def info(message: String) =
    IO.sync(logger.info(message))

  /** Logs the given [[Throwable]] as a system failure. */
  private def error(t: Throwable) =
    IO.sync(logger.error("Failed to boot Unity up", t))
}
