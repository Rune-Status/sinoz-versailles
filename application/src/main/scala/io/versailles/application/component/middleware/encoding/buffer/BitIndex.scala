package io.versailles.application.component.middleware.encoding.buffer

/**
  * The writer or reader index in a bit block.
  * @author Sino
  */
final class BitIndex(private var value: Int) {
  def add(amount: Int): Unit = {
    value += amount
  }

  def remove(amount: Int): Unit = {
    value -= amount
  }

  def toValue = value
}
