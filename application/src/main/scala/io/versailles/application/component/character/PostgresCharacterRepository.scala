package io.versailles.application.component.character

import io.versailles.domain.model.{CharacterProfile, CharacterRepository, Email}

import scala.concurrent.ExecutionContext

/**
  * A [[CharacterRepository]] that saves [[CharacterProfile]]s into
  * a PostgreSQL database server.
  * @author Sino
  */
final class PostgresCharacterRepository extends CharacterRepository {
  override def get(email: Email)(implicit ec: ExecutionContext) =
    ???

  override def update(email: Email, character: CharacterProfile)(implicit ec: ExecutionContext) =
    ???
}
