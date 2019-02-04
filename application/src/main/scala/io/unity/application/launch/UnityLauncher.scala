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
  override def run(args: List[String]) =
    launch
      .attempt
      .map(_.fold(_ => ExitStatus.ExitWhenDone(1, 1.minute), _ => ExitStatus.ExitNow(0)))

  private def launch: IO[Exception, Unit] =
    for {
      cache         <- loadAssetStorage(Paths.get("resources/assets/oldschool/177/storage/"))
      archiveCount  <- IO.sync(cache.getArchiveCount)

      _             <- putStrLn(s"$archiveCount")
    } yield ()

  private def loadAssetStorage(path: Path) =
    IO.sync(new Cache(FileStore.open(path.toFile)))
}
