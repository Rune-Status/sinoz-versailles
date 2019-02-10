package io.versailles.application.component.middleware.encoding

import io.netty.buffer.ByteBuf

/**
  * A package object to contains extension functions for the [[ByteBuf]] type.
  * @author Sino
  */
package object buffer {
  implicit final class RichByteBuf(val buf: ByteBuf) extends AnyVal {
    /** Reads a [[String]] from the buffer that is terminated by a NULL value. */
    def readCString(): String = {
      val bldr = new StringBuilder
      while (buf.isReadable) {
        val character = buf.readUnsignedByte()
        if (character == 0) {
          return bldr.toString()
        }

        bldr.append(character.toChar)
      }

      bldr.toString()
    }

    /** Reads a [[String]] from the buffer that is encoded with a NULL value
      * at both the front and the tail of the sequence. */
    def readDoubleEndedCString: String = {
      val terminatorValue = buf.readUnsignedByte()
      if (terminatorValue != 0) {
        throw new Exception("not a double ended c-string")
      }

      readCString()
    }

    /** Engages in writing a variable sized data block where the size
      * of the block is prefixed beforehand as an 8-bit integer. */
    def byteBlock(block: => Unit): Unit = {
      val offset = buf.writerIndex()

      buf.writeByte(0)

      try {
        block
      } finally {
        buf.setByte(offset, buf.writerIndex() - offset - 1)
      }
    }

    /** Engages in writing a variable sized data block where the size
      * of the block is prefixed beforehand as a singular 16 bit integer. */
    def shortBlock(block: => Unit): Unit = {
      val offset = buf.writerIndex()

      buf.writeShort(0)

      try {
        block
      } finally {
        buf.setShort(offset, buf.writerIndex() - offset - 2)
      }
    }

    /** Writes the given [[String]] in a sequence that is terminated by a
      * NULL value. */
    def writeCString(value: String) = {
      value.foreach { character =>
        buf.writeByte(character)
      }

      buf.writeByte(0)
    }

    /** Writes the given [[String]] in a sequence where both the front and
      * the tail of the sequence are marked with a NULL value. */
    def writeDoubleEndedCString(value: String) = {
      buf.writeByte(0)
      buf.writeCString(value)
    }

    /** Engages in writing a variable sized data block where the contents
      * of the block are encoded in bits. */
    def bitBlock(block: BitIndex => Unit): Unit = {
      val bitIndex = new BitIndex(buf.writerIndex() * 8)

      try {
        block(bitIndex)
      } finally {
        buf.writerIndex((bitIndex.toValue + 7) / 8)
      }
    }

    /** Writes the given flag as a single bit. */
    def writeBit(value: Boolean)(implicit bitIndex: BitIndex) =
      writeBits(1, if (value) 1 else 0)

    /** Dedicates the specified amount of bits to the specified value
      * in the buffer sequence. */
    def writeBits(amount: Int, value: Int)(implicit bitIndex: BitIndex): Unit = {
      if (value >= (1 << amount)) {
        throw new IllegalArgumentException(s"Cannot dedicate $amount bits to a value that exceeds ${1 << amount}")
      }

      var amtBits = amount

      var bytePos = bitIndex.toValue >> 3
      var bitOffset = 8 - (bitIndex.toValue & 7)

      bitIndex.add(amtBits)

      while (amtBits > bitOffset) {
        var tmp: Int = buf.getByte(bytePos)

        tmp &= ~BitMasks(bitOffset)
        tmp |= (value >> (amtBits - bitOffset)) & BitMasks(bitOffset)

        buf.setByte({bytePos += 1; bytePos - 1}, tmp.toByte)

        amtBits -= bitOffset

        bitOffset = 8
      }

      if (amtBits == bitOffset) {
        var tmp: Int = buf.getByte(bytePos)

        tmp &= ~BitMasks(bitOffset)
        tmp |= value & BitMasks(bitOffset)

        buf.setByte(bytePos, tmp.toByte)
      } else {
        var tmp: Int = buf.getByte(bytePos)

        tmp &= ~(BitMasks(amtBits) << (bitOffset - amtBits))
        tmp |= (value & BitMasks(amtBits)) << (bitOffset - amtBits)

        buf.setByte(bytePos, tmp.toByte)
      }
    }
  }

  private val BitMasks = (0 until 32) map { x => (1 << x) - 1 } toArray
}
