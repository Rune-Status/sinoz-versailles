package io.unity.application.event

import io.unity.application.model.Nonce

/**
  * A type of response from a particular kind of application service.
  * @author Sino
  */
object ServiceResponse {
  case class MayProceed(nonce: Option[Nonce]) extends Type(code = 0)
  case object ClientOutOfDate                 extends Type(code = 6)
  case object WorldFull                       extends Type(code = 7)
  abstract class Type(val code: Int)
}
