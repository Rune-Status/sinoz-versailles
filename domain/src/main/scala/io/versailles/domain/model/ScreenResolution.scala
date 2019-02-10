package io.versailles.domain.model

/**
  * The screen resolution of the client defined in pixels.
  * @author Sino
  */
case class ScreenResolution(
  pixelsWidth: Int,
  pixelsHeight: Int,
  resizable: Boolean
)
