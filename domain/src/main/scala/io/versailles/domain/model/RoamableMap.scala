package io.versailles.domain.model

object RoamableMap {
  /** The size of the map in tiles on each axis. */
  val Size = 104

  /** The size of the boundary from the edges of the map. */
  val BoundarySize = 16

  /** The points where each type of boundary starts. */
  val LowerBoundary = BoundarySize
  val UpperBoundary = Size - BoundarySize
}

/**
  * A 104 by 104 area of tiles that the player can currently roam.
  * @author Sino
  */
case class RoamableMap(lastRecenter: Position.TileScope) {
  import RoamableMap._

  def recenter(position: Position.TileScope) =
    copy(lastRecenter = position)

  def reachedEdge(position: Position.TileScope) = {
    val mapX = position.xRelativeFrom(lastRecenter)
    val mapY = position.yRelativeFrom(lastRecenter)

    val reachedUpperEdge = mapX >= UpperBoundary || mapY >= UpperBoundary
    val reachedLowerEdge = mapX < LowerBoundary || mapY < LowerBoundary

    reachedLowerEdge || reachedUpperEdge
  }
}