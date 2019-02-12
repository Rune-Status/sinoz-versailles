package io.versailles.domain.event

import io.versailles.domain.model.PID

/**
  * An event of a [[PID]] being reserved for a particular user.
  * @author Sino
  */
case class PIDReserved(pid: PID)
