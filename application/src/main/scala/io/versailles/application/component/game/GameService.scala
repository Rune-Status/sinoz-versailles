package io.versailles.application.component.game

import akka.actor.{Actor, Props}
import io.versailles.application.component.account.AccountService
import io.versailles.application.component.character.CharacterService
import io.versailles.domain.command.ReservePID
import io.versailles.domain.event.{PIDReserved, WorldIsFull}

object GameService {
  /** The amount of players and npcs allowed to exist in the
    * game world at once. */
  val PlayerCapacity = 1 << 11
  val NpcCapacity = 1 << 15

  def props(accountService: AccountService, characterService: CharacterService) =
    Props(new GameService(accountService, characterService))
}

/**
  * A service that serves gameplay.
  * @author Sino
  */
final class GameService(accountService: AccountService, characterService: CharacterService) extends Actor {
  import GameService._

  /** The repositories of player and npc [[io.versailles.domain.model.PID]]s. */
  val playerPIDs = new StackfulPIDRepository(PlayerCapacity)
  val npcPIDs = new StackfulPIDRepository(NpcCapacity)

  override def receive = {
    case ReservePID =>
      val pidOpt = playerPIDs.obtain()
      if (pidOpt.isEmpty) {
        sender() ! WorldIsFull
      } else {
        sender() ! PIDReserved(pidOpt.get)
      }
  }
}
