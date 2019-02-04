package io.unity.domain.model

import scalaz.zio.IO

/**
  * A repository of [[Account]]s.
  * @author Sino
  */
trait AccountRepository {
  def get(email: Email): IO[Exception, Option[Account]]
  def update(email: Email, account: Account): IO[Exception, Boolean]
}
