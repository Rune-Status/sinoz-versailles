package io.unity.domain.model

import cats.effect.IO

/**
  * A repository of [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterRepository {
  def get(email: Email): IO[Option[CharacterProfile]]
  def update(email: Email, character: CharacterProfile): IO[Boolean]
}
