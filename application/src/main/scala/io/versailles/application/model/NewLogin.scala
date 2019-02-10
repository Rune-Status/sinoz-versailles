package io.versailles.application.model

import io.versailles.domain.model.{AccountRank, IronMan, PID, Position}

/**
  * Embodies all of the necessary information for the client on a fresh new login.
  * @author Sino
  */
case class NewLogin(
  pid: PID,
  rank: Option[AccountRank.Type],
  ironMan: Option[IronMan.Type],
  position: Position,
  xteaKeySets: Seq[Vector[Int]],
  playerLocations: Seq[Int]
)
