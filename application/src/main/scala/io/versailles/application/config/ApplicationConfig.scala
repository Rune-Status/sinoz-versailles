package io.versailles.application.config

import java.nio.file.{Path, Paths}

import com.twitter.conversions.StorageUnitOps._
import com.twitter.util.StorageUnit
import com.typesafe.config.{Config, ConfigFactory}
import io.versailles.application.model.{ClientVersion, CredentialBlockKeySet}

object ApplicationConfig {
  def load(path: Path) =
    hoconConfigToApplicationConfig(loadHoconFile(path))

  private def hoconConfigToApplicationConfig(config: Config) =
    ApplicationConfig(
      clientVersion = ClientVersion(config.getInt("versailles.client.expected-version")),
      storagePath = Paths.get(config.getString("versailles.assets.storage.path")),

      assetsServeLimit = 1024.kilobytes, // TODO load from config

      credentialsKeySet = CredentialBlockKeySet(
        modulus = BigInt(config.getString("versailles.client.login.credentials-block.rsa.modulus")),
        exponent = BigInt(config.getString("versailles.client.login.credentials-block.rsa.exponent")),
      )
    )

  private def loadHoconFile(path: Path) =
    ConfigFactory.parseFile(path.toFile)
}

/**
  * The object representation of the application configuration file.
  * @author Sino
  */
case class ApplicationConfig(
  clientVersion: ClientVersion,
  storagePath: Path,
  assetsServeLimit: StorageUnit,
  credentialsKeySet: CredentialBlockKeySet
)
