package io.versailles.application.component.character

import io.versailles.domain.model.{CharacterProfile, Email}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.Duration

/**
  * A cache that temporarily stores [[CharacterProfile]]s.
  * @author Sino
  */
trait CharacterCache {
  def get(email: Email)(implicit ec: ExecutionContext): Future[Option[CharacterProfile]]
  def put(email: Email, profile: CharacterProfile, expireAfter: Duration)(implicit ec: ExecutionContext): Future[Boolean]
}
