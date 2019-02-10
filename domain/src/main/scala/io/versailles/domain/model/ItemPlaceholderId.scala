package io.versailles.domain.model

/**
  * The id of a replicate of the original [[Item]]. The replicate can be used
  * as a placeholder in a player's bank.
  * @author Sino
  */
case class ItemPlaceholderId(private val value: Int) extends AnyVal {
  def toValue = value
}
