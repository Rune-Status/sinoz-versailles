package io.unity.domain.model

/**
  * The id of a npc.
  * @author Sino
  */
case class NpcId(private val value: Int) extends AnyVal {
  def toValue = value
}
