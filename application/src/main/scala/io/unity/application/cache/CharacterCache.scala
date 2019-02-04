package io.unity.application.cache

import cats.effect.IO
import io.unity.domain.model.{CharacterProfile, Email}

import scala.concurrent.duration.Duration

/**
  * A cache that temporarily stores [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterCache {
  def get(email: Email): IO[Option[CharacterProfile]]
  def put(email: Email, profile: CharacterProfile, expireAfter: Duration): IO[Boolean]
}
