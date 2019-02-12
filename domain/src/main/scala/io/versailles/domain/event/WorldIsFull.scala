package io.versailles.domain.event

/**
  * An event of the game world being full and has thus ran out of PID's
  * to reserve for new users.
  * @author Sino
  */
case object WorldIsFull extends Exception