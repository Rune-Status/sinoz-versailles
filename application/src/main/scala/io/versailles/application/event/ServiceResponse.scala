package io.versailles.application.event

import io.versailles.application.model.{NewLogin, Nonce, Reconnection}

/**
  * A type of response from a particular kind of application service.
  * @author Sino
  */
object ServiceResponse {
  case class MayProceed(nonce: Option[Nonce])             extends Type(code = 0)
  case class NewLoginAccepted(details: NewLogin)          extends Type(code = 2)
  case object ClientOutOfDate                             extends Type(code = 6)
  case object WorldFull                                   extends Type(code = 7)
  case class ReconnectionAccepted(details: Reconnection)  extends Type(code = 15)

  sealed abstract class Type(val code: Int)
}
