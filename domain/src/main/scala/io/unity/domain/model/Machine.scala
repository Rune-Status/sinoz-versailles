package io.unity.domain.model

import com.twitter.util.StorageUnit
import io.unity.domain.model.Machine.Hardware.{CPU, RAM}
import io.unity.domain.model.Machine.Software.{JavaVersion, OperatingSystem, OperatingSystemVersion, Vendor}

object Machine {
  object Hardware {
    /** Contains information about the CPU. */
    case class CPU(cores: Int, bit64Architecture: Boolean)

    /** Represents the RAM. */
    case class RAM(private val value: StorageUnit) extends AnyVal {
      def inBytes = value.bytes
      def inGigaBytes = value.inGigabytes
      def inKiloBytes = value.inKilobytes
      def inMegaBytes = value.inMegabytes
      def inTeraBytes = value.inTerabytes
    }
  }

  object Software {
    /** The version of the Java software installed on the machine. */
    case class JavaVersion(
      major: Int,
      minor: Int,
      patch: Int
    )

    /** The type of vendor.  */
    object Vendor {
      case object Microsoft extends Type
      case object Sun extends Type
      case object Other extends Type

      sealed abstract class Type
    }

    /** The type of operating system that is installed on the machine. */
    object OperatingSystem {
      case object Windows extends Type
      case object MacOS extends Type
      case object Linux extends Type
      case object Other extends Type

      sealed abstract class Type
    }

    /** The software version of the [[OperatingSystem]]. */
    case class OperatingSystemVersion(private val value: String) extends AnyVal
  }
}

/**
  * Contains information about the machine a client is operating on.
  * @author Sino
  */
case class Machine(
  cpu: CPU,
  ram: RAM,
  operatingSystem: OperatingSystem.Type,
  operatingSystemVersion: OperatingSystemVersion,
  vendor: Vendor.Type,
  javaVersion: JavaVersion
)
