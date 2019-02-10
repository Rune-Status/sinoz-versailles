package io.versailles.application.component.middleware.encoding.crypto

object Marshalling {
  val ValidCharacters = Array[Char]('_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
}

/** @author Sino */
trait Marshalling {
  import Marshalling._

  final def demarshall(hash: Long): String = {
    if (hash == 0 || hash % 37L == 0L) {
      ""
    } else {
      val characters = new Array[Char](12)

      var i = 0
      var l = hash
      while (l != 0L) {
        val l1 = l
        l /= 37L

        characters(11 - i) = ValidCharacters((l1 - l * 37L).toInt)
        i += 1
      }

      new String(characters, 12 - i, i)
    }
  }

  final def marshall(value: String): Long = {
    var result = 0L
    var i = 0
    while (i < value.length && i < 12) {
      val character = value.charAt(i)
      result *= 37L

      if (character >= 'A' && character <= 'Z') {
        result += (1 + character) - 65
      } else if (character >= 'a' && character <= 'z') {
        result += (1 + character) - 97
      } else if (character >= '0' && character <= '9') {
        result += (27 + character) - 48
      }

      i += 1
    }

    while (result % 37L == 0L && result != 0L) {
      result /= 37L
    }

    result
  }
}