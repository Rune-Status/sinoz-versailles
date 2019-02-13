package io.versailles.domain.model

/**
  * A repository of [[PID]]s.
  * @author Sino
  */
trait PIDRepository {
  def obtain(): Option[PID]

  def release(pid: PID)

  def isEmpty(): Boolean
  def nonEmpty(): Boolean

  def size(): Int
}
