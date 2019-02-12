package io.versailles.application.component.middleware.handler

import akka.actor.{Actor, Props}
import io.versailles.application.command.RequestLogin
import io.versailles.application.model.Nonce

object LoginRequestHandler {
  def props(expectedNonce: Nonce) = Props(new LoginRequestHandler(expectedNonce))
}

/**
  * TODO
  * @author Sino
  */
final class LoginRequestHandler(expectedNonce: Nonce) extends Actor {
  override def receive = {
    case RequestLogin(clientState, request) =>
      // TODO
  }
}
