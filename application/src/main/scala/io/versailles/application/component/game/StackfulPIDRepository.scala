package io.versailles.application.component.game

import io.versailles.domain.model.{PID, PIDRepository}

/**
  * A [[PIDRepository]] that stores [[PID]]s in a stack-like data structure.
  * @author Sino
  */
final class StackfulPIDRepository(initialSize: Int) extends PIDRepository {
  private var pidStack = (1 to initialSize).map(PID).toList

  override def obtain() = {
    val result = pidStack.headOption
    if (result.isDefined) {
      pidStack = pidStack.drop(1)
    }

    result
  }

  override def release(pid: PID): Unit = pidStack +:= pid

  override def size() = pidStack.size

  override def isEmpty() = pidStack.isEmpty

  override def nonEmpty() = pidStack.nonEmpty
}
