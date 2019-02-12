package io.versailles.application.component.game

import akka.actor.{Actor, Props}
import io.versailles.domain.command.ReservePID

object GameService {
  def props = Props(new GameService)
}

/**
  * A service that serves gameplay.
  * @author Sino
  */
final class GameService extends Actor {
  override def receive = {
    case ReservePID =>
      // TODO
  }
}
