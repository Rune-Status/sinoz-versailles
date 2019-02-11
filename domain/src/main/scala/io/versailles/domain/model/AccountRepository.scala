package io.versailles.domain.model

import scala.concurrent.Future

/**
  * A repository of [[Account]]s.
  * @author Sino
  */
trait AccountRepository {
  def get(email: Email): Future[Option[Account]]
  def update(email: Email, account: Account): Future[Boolean]
}
