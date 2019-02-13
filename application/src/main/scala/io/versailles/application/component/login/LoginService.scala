package io.versailles.application.component.login

import akka.pattern.pipe
import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import io.versailles.application.command.{LogUserIn, ReconnectUser}
import io.versailles.application.component.auth.AuthenticationService
import io.versailles.application.component.character.CharacterService
import io.versailles.application.model.PinCodeInput
import io.versailles.domain.command.ReservePID
import io.versailles.domain.event._
import io.versailles.domain.model.{Email, PID, Password}

import scala.async.Async.{async, await}
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Promise}
import scala.util.{Failure, Success}

object LoginService {
  def props(authService: AuthenticationService, characterService: CharacterService, gameService: ActorRef) =
    Props(new LoginService(authService, characterService, gameService))
}

/**
  * A service that deals with logging users in.
  * @author Sino
  */
final class LoginService(authService: AuthenticationService, characterService: CharacterService, gameService: ActorRef) extends Actor {
  /** The amount of time to wait until the pid reservation request is cancelled
    * or considered as failed. */
  private val pidReserveTimeout = Timeout(2.seconds)

  override def receive = {
    case LogUserIn(email, password, pinCodeInput) =>
      val client = sender()

      // required for pipeTo
      import context.dispatcher

      // attempt to login and when it is completed, pipe result back to the caller
      // in case a strange failure has occurred, it is wrapped with an ADT to give
      // it some meaning
      login(email, password, pinCodeInput)
        .recover { case t: Throwable => CouldNotCompleteLogin(t) }
        .pipeTo(client)

    case ReconnectUser() =>
      // TODO
  }

  /** Attempts to log into the game world using the given credentials for
    * authentication and looking up account information. */
  def login(email: Email, password: Password, pinCodeInput: Option[PinCodeInput.Type])(implicit ec: ExecutionContext) =
    async {
      val account = await(authService.authenticate(email, password, pinCodeInput))
      val pid = await(reservePID)

      val characterProfile = await(characterService.getCharacter(email))
      if (characterProfile.isEmpty) {
        throw CouldNotFindCharacterProfile
      }

      UserLoggedIn(account, characterProfile.get, pid)
    }

  /** Attempts to ask the [[gameService]] to reserve a PID for a new user
    * that is about to login. */
  private def reservePID(implicit ec: ExecutionContext) = {
    val pidPromise = Promise[PID]()
    val pidReservation = gameService.ask(ReservePID)(pidReserveTimeout)

    pidReservation.onComplete {
      case Success(PIDReserved(pid)) =>
        pidPromise.success(pid)

      case Success(otherwise: Throwable) =>
        pidPromise.failure(otherwise)

      case Failure(e) =>
        pidPromise.failure(CouldNotCompleteLogin(e))
    }

    pidPromise.future
  }
}
