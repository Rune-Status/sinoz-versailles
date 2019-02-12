package io.versailles.domain.model

import scala.concurrent.{ExecutionContext, Future}

/**
  * A repository of [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterRepository {
  def get(email: Email)(implicit ec: ExecutionContext): Future[Option[CharacterProfile]]
  def update(email: Email, character: CharacterProfile)(implicit ec: ExecutionContext): Future[Boolean]
}
