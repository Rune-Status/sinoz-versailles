package io.versailles.application.component.middleware.handler

import akka.actor.{Actor, Props}
import io.versailles.application.command.RequestLogin
import io.versailles.application.model.{ArchiveChecksum, Nonce}

object LoginRequestHandler {
  def props(expectedNonce: Nonce, expectedArchiveChecksums: Seq[ArchiveChecksum]) =
    Props(new LoginRequestHandler(expectedNonce, expectedArchiveChecksums))
}

/**
  * The application logic handler that processes requests
  * to log into the game world.
  * @author Sino
  */
final class LoginRequestHandler(expectedNonce: Nonce, expectedArchiveChecksums: Seq[ArchiveChecksum]) extends Actor {
  override def receive = {
    case RequestLogin(clientState, request) =>
      // TODO
  }
}
