package io.versailles.domain.model

import scalaz.zio.IO

/**
  * A repository of [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterRepository {
  def get(email: Email): IO[Exception, Option[CharacterProfile]]
  def update(email: Email, character: CharacterProfile): IO[Exception, Boolean]
}
