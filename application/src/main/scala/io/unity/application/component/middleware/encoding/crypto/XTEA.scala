package io.unity.application.component.middleware.encoding.crypto

import io.netty.buffer.ByteBuf

object XTEA {
  val GoldenRatio = 0x9E3779B9
  val Rounds = 32
}

/** @author Sino */
trait XTEA {
  import XTEA._

  final def xteaDecrypt(buffer: ByteBuf, start: Int, end: Int, keys: Vector[Int]): Unit = {
    val amtQuads = (end - start) / 8

    0 until amtQuads foreach { quad =>
      var sum = GoldenRatio * Rounds

      var v0 = buffer.getInt(start + quad * 8)
      var v1 = buffer.getInt(start + quad * 8 + 4)

      0 until Rounds foreach { round =>
        v1 -= (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + keys((sum >>> 11) & 3))
        sum -= GoldenRatio
        v0 -= (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + keys(sum & 3))
      }

      buffer.setInt(start + quad * 8, v0)
      buffer.setInt(start + quad * 8 + 4, v1)
    }
  }

  final def xteaEncrypt(buffer: ByteBuf, start: Int, end: Int, keys: Vector[Int]): Unit = {
    val amtQuads = (end - start) / 8

    0 until amtQuads foreach { quad =>
      var sum = GoldenRatio * Rounds

      var v0 = buffer.getInt(start + quad * 8)
      var v1 = buffer.getInt(start + quad * 8 + 4)

      0 until Rounds foreach { round =>
        v0 += (((v1 << 4) ^ (v1 >>> 5)) + v1) ^ (sum + keys(sum & 3))
        sum += GoldenRatio
        v1 += (((v0 << 4) ^ (v0 >>> 5)) + v0) ^ (sum + keys((sum >>> 11) & 3))
      }

      buffer.setInt(start + quad * 8, v0)
      buffer.setInt(start + quad * 8 + 4, v1)
    }
  }
}
