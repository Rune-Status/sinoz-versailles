package io.unity.domain.command

import io.unity.domain.model.{CommandArgument, CommandName}

/**
  * A command that denotes a game command is to be submitted.
  * @author Sino
  */
case class SubmitGameCommand(
  name: CommandName,
  arguments: Seq[CommandArgument]
)
