package io.versailles.domain.model

import scala.concurrent.{ExecutionContext, Future}

/**
  * A repository of [[Account]]s.
  * @author Sino
  */
trait AccountRepository {
  def get(email: Email)(implicit ec: ExecutionContext): Future[Option[Account]]
  def update(email: Email, account: Account)(implicit ec: ExecutionContext): Future[Boolean]
}
