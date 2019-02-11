package io.versailles.domain.model

import scala.concurrent.Future

/**
  * A repository of [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterRepository {
  def get(email: Email): Future[Option[CharacterProfile]]
  def update(email: Email, character: CharacterProfile): Future[Boolean]
}
