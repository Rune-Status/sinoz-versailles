package io.unity.domain.command

/**
  * A command that denotes a game command is to be submitted.
  * @author Sino
  */
case class SubmitGameCommand(
  head: String,
  arguments: Seq[String]
)
