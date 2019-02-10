package io.versailles.application.launch

import java.nio.file.{Path, Paths}

import com.typesafe.config.{Config, ConfigFactory}
import io.versailles.application.model.{ClientVersion, CredentialBlockKeySet}
import scalaz.zio.IO

object VersaillesConfig {
  def load(path: Path): IO[Exception, VersaillesConfig] = {
    val pathString = path.toString
    if (pathString.endsWith(".conf")) {
      for {
        hoconConfig <- loadHoconFile(path)
        unityConfig <- hoconConfigToVersaillesConfig(hoconConfig)
      } yield unityConfig
    } else {
      IO.fail(new UnsupportedOperationException())
    }
  }

  private def hoconConfigToVersaillesConfig(config: Config): IO[Exception, VersaillesConfig] =
    IO.succeed(VersaillesConfig(
      clientVersion = ClientVersion(config.getInt("versailles.client.expected-version")),
      storagePath = Paths.get(config.getString("versailles.assets.storage.path")),

      credentialsKeySet = CredentialBlockKeySet(
        modulus = BigInt(config.getString("versailles.client.login.credentials-block.rsa.modulus")),
        exponent = BigInt(config.getString("versailles.client.login.credentials-block.rsa.exponent")),
      )
    ))

  private def loadHoconFile(path: Path): IO[Exception, Config] =
    IO.syncException(ConfigFactory.parseFile(path.toFile))
}

/**
  * The object representation of the Versailles configuration file.
  * @author Sino
  */
case class VersaillesConfig(
  clientVersion: ClientVersion,
  storagePath: Path,
  credentialsKeySet: CredentialBlockKeySet
)
