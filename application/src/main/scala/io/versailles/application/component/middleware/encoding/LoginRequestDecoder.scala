package io.versailles.application.component.middleware.encoding

import com.twitter.conversions.StorageUnitOps._
import io.netty.buffer.ByteBuf
import io.versailles.application.command.RequestLogin
import io.versailles.application.component.middleware.MessageDecoder
import io.versailles.application.component.middleware.MessageDecoder.Result.{MessageOutput, RequireMoreBytes}
import io.versailles.application.component.middleware.encoding.buffer._
import io.versailles.application.component.middleware.encoding.crypto.{RSA, XTEA}
import io.versailles.application.model.PinCodeInput.{EnteredPinCode, StoredPinCodeHash}
import io.versailles.application.model._
import io.versailles.domain.model.Machine.Hardware.{CPU, RAM}
import io.versailles.domain.model.Machine.Software.{JavaVersion, OperatingSystem, OperatingSystemVersion, Vendor}
import io.versailles.domain.model.{Email, Machine, Password, ScreenResolution}

/**
  * A [[MessageDecoder]] that translates a stream of bytes into a
  * [[RequestLogin]] command.
  * @author Sino
  */
final class LoginRequestDecoder(archiveCount: Int, keySet: CredentialBlockKeySet) extends MessageDecoder with RSA with XTEA {
  val NewLoginId = 16
  val ReconnectingId = 18

  override def decode(in: ByteBuf): MessageDecoder.Result.Type = {
    val clientState = toClientState(in.readUnsignedByte())
    if (in.readableBytes() < 2) {
      return RequireMoreBytes
    }

    val payloadSize = in.readUnsignedShort()
    if (payloadSize > 512) {
      throw new Exception("login payload block length larger than 512 bytes")
    }

    if (in.readableBytes() < payloadSize) {
      return RequireMoreBytes
    }

    val clientVersion = ClientVersion(in.readInt())

    in.readInt()
    in.readByte()

    val rsaBlockSize = in.readUnsignedShort()
    val rsaBlock = in.readSlice(rsaBlockSize)

    val rsaDecrypted = rsaCrypt(rsaBlock)(keySet.modulus, keySet.exponent)
    val rsaBlockPrefix = rsaDecrypted.readUnsignedByte()
    if (rsaBlockPrefix != 1) {
      throw new Exception()
    }

    val nonce = Nonce(rsaDecrypted.readLong())

    val xteaBlockKeySet = (0 until 4).map(_ => rsaDecrypted.readInt())
    val seeds = xteaBlockKeySet.map(Seed)

    var previousSeeds: Option[Seq[Seed]] = None

    var password: Option[Password] = None
    var pinCodeInputType: Option[PinCodeInput.Type] = None

    if (clientState == ClientState.Reconnecting) {
      previousSeeds = Some((0 until 4).map(_ => Seed(rsaDecrypted.readInt())))
    } else {
      val inputTypeId = rsaDecrypted.readUnsignedByte()

      pinCodeInputType = inputTypeId match {
        case 0 =>
          Some(StoredPinCodeHash(rsaDecrypted.readInt()))

        case 2 =>
          rsaDecrypted.skipBytes(4)
          None

        case 1 | 3 =>
          val pinCode = PinCode(rsaDecrypted.readUnsignedMedium())
          val trustFor30Days = inputTypeId == 1

          rsaDecrypted.skipBytes(1)
          Some(EnteredPinCode(pinCode, trustFor30Days))
      }

      rsaDecrypted.readUnsignedByte()
      password = Some(Password(rsaDecrypted.readCString()))
    }

    val xteaBlock = in.readSlice(in.readableBytes())

    xteaDecrypt(xteaBlock, 0, xteaBlock.readableBytes(), xteaBlockKeySet.toVector)

    val email = Email(xteaBlock.readCString())

    val clientFlags = xteaBlock.readUnsignedByte()

    val isResizable = (clientFlags >> 1) == 1
    val lowMemoryVersion = (clientFlags & 1) == 1

    val screenWidth = xteaBlock.readShort()
    val screenHeight = xteaBlock.readShort()

    val screenResolution = ScreenResolution(screenWidth, screenHeight, isResizable)

    val uid = UID((0 until 24).map(_ => xteaBlock.readByte().toInt))

    xteaBlock.readCString()
    xteaBlock.readInt()

    val machineInfoBlockPrefix = xteaBlock.readUnsignedByte()
    if (machineInfoBlockPrefix != 7) {
      throw new Exception()
    }

    val operatingSystem = toOperatingSystem(xteaBlock.readUnsignedByte())
    val bit64Architecture = xteaBlock.readBoolean()
    val osVersion = toOperatingSystemVersion(operatingSystem, xteaBlock.readUnsignedByte())
    val vendor = toVendor(xteaBlock.readUnsignedByte())

    val javaVersion = JavaVersion(
      major = xteaBlock.readUnsignedByte(),
      minor = xteaBlock.readUnsignedByte(),
      patch = xteaBlock.readUnsignedByte()
    )

    xteaBlock.readBoolean()

    val ram = RAM(xteaBlock.readUnsignedShort().gigabytes)
    val cpu = CPU(xteaBlock.readUnsignedByte(), bit64Architecture)

    xteaBlock.readMedium()
    xteaBlock.readShort()

    xteaBlock.readDoubleEndedCString
    xteaBlock.readDoubleEndedCString
    xteaBlock.readDoubleEndedCString
    xteaBlock.readDoubleEndedCString

    xteaBlock.readByte()
    xteaBlock.readShort()

    xteaBlock.readDoubleEndedCString
    xteaBlock.readDoubleEndedCString

    xteaBlock.readByte()
    xteaBlock.readByte()

    xteaBlock.readInt()
    xteaBlock.readInt()
    xteaBlock.readInt()

    xteaBlock.readInt()
    xteaBlock.readDoubleEndedCString

    val machine = Machine(cpu, ram, operatingSystem, osVersion, vendor, javaVersion)

    xteaBlock.readByte()
    xteaBlock.readInt()

    val checksums = (0 until archiveCount).map(_ => ArchiveChecksum(xteaBlock.readInt()))

    val request = LoginRequest(email, password, nonce, pinCodeInputType, seeds, previousSeeds, checksums, clientVersion, screenResolution, uid, machine)

    MessageOutput(RequestLogin(clientState, request))
  }

  private def toClientState(id: Int) =
    id match {
      case NewLoginId     => ClientState.NewLogin
      case ReconnectingId => ClientState.Reconnecting
      case otherwise      => throw new IllegalArgumentException
    }

  private def toOperatingSystem(id: Int) =
    id match {
      case 1          => OperatingSystem.Windows
      case 2          => OperatingSystem.MacOS
      case 3          => OperatingSystem.Linux
      case otherwise  => OperatingSystem.Other
    }

  private def toOperatingSystemVersion(os: OperatingSystem.Type, id: Int) =
    os match {
      case OperatingSystem.Windows =>
        id match {
          case 1 =>
            OperatingSystemVersion("4.0")

          case 2 =>
            OperatingSystemVersion("4.1")

          case 3 =>
            OperatingSystemVersion("4.9")

          case 4 =>
            OperatingSystemVersion("5.0")

          case 5 =>
            OperatingSystemVersion("5.1")

          case 6 =>
            OperatingSystemVersion("5.2")

          case 7 =>
            OperatingSystemVersion("6.0")

          case 8 =>
            OperatingSystemVersion("6.1")

          case 9 =>
            OperatingSystemVersion("6.2")

          case 10 =>
            OperatingSystemVersion("6.3")

          case 11 =>
            OperatingSystemVersion("10.0")

          case otherwise =>
            OperatingSystemVersion("Unknown")
        }

      case OperatingSystem.MacOS =>
        id match {
          case 20 =>
            OperatingSystemVersion("10.4")

          case 21 =>
            OperatingSystemVersion("10.5")

          case 22 =>
            OperatingSystemVersion("10.6")

          case 23 =>
            OperatingSystemVersion("10.7")

          case 24 =>
            OperatingSystemVersion("10.8")

          case 25 =>
            OperatingSystemVersion("10.9")

          case 26 =>
            OperatingSystemVersion("10.10")

          case 27 =>
            OperatingSystemVersion("10.11")

          case 28 =>
            OperatingSystemVersion("10.12")

          case 29 =>
            OperatingSystemVersion("10.13")

          case otherwise =>
            OperatingSystemVersion("Unknown")
        }

      case otherwise =>
        OperatingSystemVersion("Unknown")
    }

  private def toVendor(id: Int) =
    id match {
      case 1          => Vendor.Sun
      case 2          => Vendor.Microsoft
      case 3          => Vendor.Apple
      case 4          => Vendor.Other
      case 5          => Vendor.Oracle
    }
}
