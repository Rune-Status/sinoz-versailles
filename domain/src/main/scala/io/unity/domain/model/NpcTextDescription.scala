package io.unity.domain.model

/**
  * The textual description of a npc. This is used for
  * scenarios such as examining an npc.
  * @author Sino
  */
case class NpcTextDescription(private val value: String) extends AnyVal {
  def toValue = value
}
