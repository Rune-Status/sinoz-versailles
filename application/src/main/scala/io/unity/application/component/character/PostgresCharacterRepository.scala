package io.unity.application.component.character

import io.unity.domain.model
import io.unity.domain.model.{CharacterRepository, Email}

/**
  * TODO
  * @author Sino
  */
final class PostgresCharacterRepository extends CharacterRepository {
  override def get(email: Email) = ???
  override def update(email: Email, character: model.CharacterProfile) = ???
}
