package io.versailles.application.launch

import java.nio.file.{Path, Paths}

import com.typesafe.config.{Config, ConfigFactory}
import io.versailles.application.model.{ClientVersion, CredentialBlockKeySet}

object VersaillesConfig {
  def load(path: Path) =
    hoconConfigToVersaillesConfig(loadHoconFile(path))

  private def hoconConfigToVersaillesConfig(config: Config) =
    VersaillesConfig(
      clientVersion = ClientVersion(config.getInt("versailles.client.expected-version")),
      storagePath = Paths.get(config.getString("versailles.assets.storage.path")),

      credentialsKeySet = CredentialBlockKeySet(
        modulus = BigInt(config.getString("versailles.client.login.credentials-block.rsa.modulus")),
        exponent = BigInt(config.getString("versailles.client.login.credentials-block.rsa.exponent")),
      )
    )

  private def loadHoconFile(path: Path) =
    ConfigFactory.parseFile(path.toFile)
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
