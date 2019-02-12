package io.versailles.application.component.middleware

object Handler {
  type AnyCommand = Any
}

/**
  * Handles a message of type [[Command]].
  * @author Sino
  */
trait Handler[Command] { self =>
  private type NextHandler = Handler[_ <: Any]

  def handle(command: Command): Option[NextHandler]

  protected final def transitionTo(handler: NextHandler) = Some(handler)

  protected final def stop(): Option[NextHandler] = None

  protected final def stay(): Option[NextHandler] = Some(self)
}
