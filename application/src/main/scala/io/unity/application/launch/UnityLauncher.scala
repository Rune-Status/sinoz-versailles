package io.unity.application.launch

import java.nio.file.{Path, Paths}

import io.unity.application.storage.{Cache, FileStore}
import scalaz.zio.duration._
import scalaz.zio.console._
import scalaz.zio.{App, IO}

/**
  * The main entry point to this game server application.
  * @author Sino
  */
object UnityLauncher extends App {
  /** The [[Path]] to the main Unity configuration file. */
  val pathToUnityConfig = Paths.get("resources/configs/unity.conf")

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
      config        <- loadUnityConfig(pathToUnityConfig)
      _             <- info(s"$config")

      cache         <- loadAssetStorage(config.storagePath)
      archiveCount  <- IO.sync(cache.getArchiveCount)

      _             <- info(s"$archiveCount")
    } yield ()

  /** Attempts to load a [[UnityConfig]] from a local configuration
    * file on disk. */
  private def loadUnityConfig(path: Path) =
    UnityConfig.load(path)

  /** Attempts to synchronously load assets from the binary storage
    * into memory. */
  private def loadAssetStorage(path: Path) =
    IO.sync(new Cache(FileStore.open(path.toFile)))

  /** Logs the given info message. */
  private def info(message: String) =
    putStrLn(s"[INFO]:[Launch]: $message") // TODO swap with actual logger

  /** Logs the given [[Throwable]] as a system failure. */
  private def error(t: Throwable) =
    putStrLn(s"[ERR]:[Launch]: Caught failure: $t") // TODO swap with actual logger
}
