package io.versailles.domain.model

/**
  * A position on the game map.
  * @author Sino
  */
case class Position(x: Int, y: Int, z: Int) {
  def patchX = x / 8
  def patchY = y / 8

  def areaX = x / 64
  def areaY = y / 64

  def xWithinPatch = x & 7
  def yWithinPatch = y & 7

  def xWithinArea = x & 63
  def yWithinArea = y & 63

  def xRelativeFrom(pos: Position) = x - ((pos.patchX - 6) * 8)
  def yRelativeFrom(pos: Position) = y - ((pos.patchY - 6) * 8)
}
