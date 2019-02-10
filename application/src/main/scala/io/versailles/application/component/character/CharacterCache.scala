package io.versailles.application.component.character

import io.versailles.domain.model.{CharacterProfile, Email}
import scalaz.zio.IO

import scala.concurrent.duration.Duration

/**
  * A cache that temporarily stores [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterCache {
  def get(email: Email): IO[Exception, Option[CharacterProfile]]
  def put(email: Email, profile: CharacterProfile, expireAfter: Duration): IO[Exception, Boolean]
}
