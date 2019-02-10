package io.versailles.application.component.character

import io.versailles.domain.model
import io.versailles.domain.model.{CharacterRepository, Email}

/**
  * TODO
  * @author Sino
  */
final class PostgresCharacterRepository extends CharacterRepository {
  override def get(email: Email) = ???
  override def update(email: Email, character: io.versailles.domain.model.CharacterProfile) = ???
}
