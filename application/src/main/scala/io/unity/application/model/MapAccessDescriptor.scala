package io.unity.application.model

/**
  * Describes the access of a map in the RuneScape game cache.
  * @author Sino
  */
case class MapAccessDescriptor(
  mapX: Int,
  mapY: Int,
  mapCacheKeySet: Vector[Int]
)
