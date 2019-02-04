package io.unity.domain.event

/**
  * An event of a user having a patch within their roamable map
  * cleared of any remaining floor items.
  * @author Sino
  */
case class PatchCleared(
  patchX: Int,
  patchY: Int
)