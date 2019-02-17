package io.versailles.domain.model

import io.versailles.domain.model.Position.{AreaScope, PatchScope, TileScope}

object Position {
  /** The boundary specifications of the game map. */
  val MapWidth = 16384
  val MapHeight = 16384
  val HighestPlane = 4

  /** A [[Position]] where the orthographic scope is put onto the 64 by 64 area level. */
  case class AreaScope private(override val x: Int, override val y: Int, override val z: Int) extends Position(x, y, z) {
    override def toPatchScope = PatchScope(x * 8, y * 8, z)

    override def toTileScope = TileScope(x * 64, y * 64, z)

    override def toAreaScope = this
  }

  /** A [[Position]] where the orthographic scope is put onto the 8 by 8 patch level. */
  case class PatchScope private(override val x: Int, override val y: Int, override val z: Int) extends Position(x, y, z) {
    override def toAreaScope = AreaScope(x / 8, y / 8, z)

    override def toTileScope = TileScope(x * 8, y * 8, z)

    override def toPatchScope = this
  }

  /** A [[Position]] where the orthographic scope is put onto the 1 by 1 tile level. */
  case class TileScope private(override val x: Int, override val y: Int, override val z: Int) extends Position(x, y, z) {
    override def toAreaScope = AreaScope(x / 64, y / 64, z)

    override def toPatchScope = PatchScope(x / 8, y / 8, z)

    override def toTileScope = this

    /** Returns the delta between this [[Position]] and the given base [[Position]].
      * The given base [[Position]] is always first translated to a [[TileScope]]
      * for correctness. */
    def xRelativeFrom(base: Position) = x - (((base.toTileScope.x / 8) - 6) * 8)
    def yRelativeFrom(base: Position) = y - (((base.toTileScope.y / 8) - 6) * 8)
  }

  def area(x: Int, y: Int, z: Int = 0) = {
    if (x < 0 || x >= (MapWidth / 64) || y < 0 || y >= (MapHeight / 64) || z < 0 || z >= HighestPlane) {
      throw new Exception()
    }

    Position.AreaScope(x, y, z)
  }

  def patch(x: Int, y: Int, z: Int = 0) = {
    if (x < 0 || x >= (MapWidth / 8) || y < 0 || y >= (MapHeight / 8) || z < 0 || z >= HighestPlane) {
      throw new Exception()
    }

    Position.PatchScope(x, y, z)
  }

  def tile(x: Int, y: Int, z: Int = 0) = {
    if (x < 0 || x >= MapWidth || y < 0 || y >= MapHeight || z < 0 || z >= HighestPlane) {
      throw new Exception()
    }

    Position.TileScope(x, y, z)
  }
}

/**
  * A position on the game map.
  * @author Sino
  */
sealed abstract class Position(val x: Int, val y: Int, val z: Int) {
  def toAreaScope: AreaScope
  def toPatchScope: PatchScope
  def toTileScope: TileScope
}