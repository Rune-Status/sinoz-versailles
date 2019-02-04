package io.unity.domain.model

/**
  * A direction someone or something is facing.
  * @author Sino
  */
object Direction {
  case object North extends Type
  case object South extends Type
  case object East extends Type
  case object West extends Type
  sealed abstract class Type
}
