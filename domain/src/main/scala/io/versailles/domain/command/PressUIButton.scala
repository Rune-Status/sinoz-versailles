package io.versailles.domain.command

/**
  * A command that denotes that a user is pressing a user interface button.
  * @author Sino
  */
case class PressUIButton(
  interface: Int,
  option: Int,
  button: Int,
  component: Int,
  sub: Int
)
