package io.versailles.domain.behaviour.routine

import io.versailles.domain.model.SystemNotification

object Routine {
  /** A routine instruction to display a [[SystemNotification]] in a client
    * user's chatbox. */
  case class AddSystemNotification[A](notification: SystemNotification, tail: Routine[A]) extends Routine[A]

  /** A routine instruction to delay execution for a specified amount of ticks. */
  case class Delay[A](ticks: Int, tail: Routine[A]) extends Routine[A]

  /** A routine instruction to prompt a value from the client user. */
  case class PromptText[A](textToDisplay: String, tail: String => Routine[A]) extends Routine[A]
  case class PromptNumeric[A](textToDisplay: String, tail: Int => Routine[A]) extends Routine[A]

  /** A routine instruction to return a produced value back into the routine. */
  case class Return[A](value: () => A) extends Routine[A]

  /** Lifts the given value of [[A]] into a [[Routine]] of [[A]]. */
  def pure[A](a: A): Routine[A] = Return(() => a)
}

/**
  * A Routine is a monadic data structure that can be used to
  * compose a series of game logic instructions.
  *
  * See the following example on how to use this monad:
  *
  * ```
  * val program =
  *   for {
  *     _     <- systemNotification(s"You search the bookcase...")
  *     _     <- delay(2.ticks)
  *     _     <- systemNotification(s"You find nothing but books about PHP.")
  *     _     <- shout("Darn it.")
  *   } yield ()
  * ```
  *
  * The `program` defines a series of sequentially executed instructions. Note
  * that these instructions do not mutate any entity's state by themselves.
  * They are merely instructions that are interpreted by a host runtime.
  *
  * @author Sino
  */
trait Routine[+A] { self =>
  import Routine._

  final def map[B](f: A => B): Routine[B] =
    flatMap(a => Routine.pure(f(a)))

  final def flatMap[B](f: A => Routine[B]): Routine[B] =
    self match {
      case Return(value) =>
        f(value())

      case Delay(content, rest) =>
        Delay(content, rest.flatMap(f))

      case AddSystemNotification(notification, rest) =>
        AddSystemNotification(notification, rest.flatMap(f))

      case PromptNumeric(textToDisplay, rest) =>
        PromptNumeric(textToDisplay, line => rest(line).flatMap(f))

      case PromptText(textToDisplay, rest) =>
        PromptText(textToDisplay, line => rest(line).flatMap(f))
    }
}