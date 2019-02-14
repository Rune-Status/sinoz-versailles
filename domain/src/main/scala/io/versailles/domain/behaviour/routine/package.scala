package io.versailles.domain.behaviour

import io.versailles.domain.model.{ChatText, DisplayName, SystemNotification}

import scala.concurrent.duration.Duration

/**
  * Contains all of the routine instructions.
  * @author Sino
  */
package object routine {
  import Routine._

  /** Produces a new [[AddSystemNotification]] instruction to display a system
    * message within the client user's chatbox. */
  def systemNotification(line: String, notificationType: SystemNotification.Type = SystemNotification.Regular, interactWith: Option[DisplayName] = None): Routine[Unit] =
    AddSystemNotification(SystemNotification(notificationType, interactWith, ChatText(line)), Return(() => ()))

  /** Produces a new [[Delay]] instruction to delay the execution for
    * the specified [[Duration]]. The given [[Duration]] can be in milliseconds,
    * seconds, minutes and even hours. */
  def delay(timeUnit: Duration): Routine[Unit] =
    Delay((timeUnit.toSeconds / 600).toInt, Return(() => ()))

  /** Produces a new [[Delay]] instruction to delay the execution for
    * the specified amount of ticks. */
  def delay(ticks: Int): Routine[Unit] =
    Delay(ticks, Return(() => ()))

  /** Produces a new [[PromptNumeric]] instruction to delay the execution until
    * a numeric user input is received. */
  def promptNumeric(textToDisplay: String): Routine[Int] =
    PromptNumeric(textToDisplay, promptedInput => Return(() => promptedInput))

  /** Produces a new [[PromptNumeric]] instruction to delay the execution until
    * a text-based user input is received. */
  def promptText(textToDisplay: String): Routine[String] =
    PromptText(textToDisplay, promptedInput => Return(() => promptedInput))
}
