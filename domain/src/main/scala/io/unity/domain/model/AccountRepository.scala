package io.unity.domain.model

import cats.effect.IO

/**
  * A repository of [[Account]]s.
  * @author Sino
  */
trait AccountRepository {
  def get(email: Email): IO[Option[Account]]
  def update(email: Email, account: Account): IO[Boolean]
}
