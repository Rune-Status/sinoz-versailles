package io.unity.application.launch

import java.nio.file.{Path, Paths}

import com.typesafe.config.{Config, ConfigFactory}
import io.unity.application.model.ClientVersion
import scalaz.zio.IO

object UnityConfig {
  def load(path: Path): IO[Exception, UnityConfig] = {
    val pathString = path.toString
    if (pathString.endsWith(".conf")) {
      for {
        hoconConfig <- loadHoconFile(path)
        unityConfig <- hoconConfigToUnityConfig(hoconConfig)
      } yield unityConfig
    } else {
      IO.fail(new UnsupportedOperationException())
    }
  }

  private def hoconConfigToUnityConfig(config: Config): IO[Exception, UnityConfig] =
    IO.succeed(UnityConfig(
      clientVersion = ClientVersion(config.getInt("unity.client.expected-version")),
      storagePath = Paths.get(config.getString("unity.assets.storage.path"))
    ))

  private def loadHoconFile(path: Path): IO[Exception, Config] =
    IO.syncException(ConfigFactory.parseFile(path.toFile))
}

/**
  * The object representation of the Unity configuration file.
  * @author Sino
  */
case class UnityConfig(
  clientVersion: ClientVersion,
  storagePath: Path
)
