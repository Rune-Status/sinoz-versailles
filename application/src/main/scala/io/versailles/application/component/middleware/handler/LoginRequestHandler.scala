package io.versailles.application.component.middleware.handler

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import io.versailles.application.command.RequestLogin
import io.versailles.application.event.{IncompatibleClient, SeedsInvalidated}
import io.versailles.application.model._
import io.versailles.domain.event.InvalidatedEmail

import scala.concurrent.duration._

object LoginRequestHandler {
  def props(channel: ActorRef, expectedVersion: ClientVersion, expectedNonce: Nonce, expectedArchiveChecksums: Seq[ArchiveChecksum], loginService: ActorRef) =
    Props(new LoginRequestHandler(channel, expectedVersion, expectedNonce, expectedArchiveChecksums, loginService))
}

/**
  * The application logic handler that processes requests
  * to log into the game world.
  * @author Sino
  */
final class LoginRequestHandler(channel: ActorRef, expectedVersion: ClientVersion, expectedNonce: Nonce, expectedArchiveChecksums: Seq[ArchiveChecksum], loginService: ActorRef) extends Actor {
  type ValidationError = Any

  val userLoginTimeout = Timeout(5.seconds)

  override def receive = {
    case RequestLogin(clientState, request) =>
      // TODO add support for reconnect

      val validationErrorOpt = validate(request)
      if (validationErrorOpt.isDefined) {
        // TODO translate ValidationError to a ServiceResponded.Type
      } else {
        // TODO command `loginService` to log a user in using command LogUserIn
      }
  }

  /** Validates the given [[LoginRequest]], potentially returning a
    * [[ValidationError]] to indicate something went wrong. */
  def validate(request: LoginRequest): Option[ValidationError] = {
    if (!request.email.isValid) {
      Some(InvalidatedEmail)
    } else if (!request.clientVersion.isUpToDateWith(expectedVersion)) {
      Some(IncompatibleClient)
    } else if (!request.seeds.forall(_.isValid)) {
      Some(SeedsInvalidated)
    } else if (!validArchiveChecksums(request.archiveChecksums)) {
      Some(IncompatibleClient)
    } else {
      None
    }
  }

  /** Checks if the given sequence of [[ArchiveChecksum]]s are perfectly matching
    * with the [[expectedArchiveChecksums]]s. */
  def validArchiveChecksums(checksums: Seq[ArchiveChecksum]) =
    checksums
      .zip(expectedArchiveChecksums)
      .forall { case (received, expected) => received == expected }
}
