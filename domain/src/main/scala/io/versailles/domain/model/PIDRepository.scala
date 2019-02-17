package io.versailles.domain.model

/**
  * A repository of [[PID]]s.
  * @author Sino
  */
trait PIDRepository {
  def obtain(): Option[PID]
  def release(pid: PID)

  def size(): Int

  def isEmpty = size == 0
  def nonEmpty = size > 0
}
