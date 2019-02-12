package io.versailles.application.component.account

import io.versailles.domain.model.{Account, AccountRepository, Email}

import scala.concurrent.ExecutionContext

/**
  * An [[AccountRepository]] that stores [[Account]]s in a PostgreSQL database.
  * @author Sino
  */
final class PostgresAccountRepository extends AccountRepository {
  override def get(email: Email)(implicit ec: ExecutionContext) =
    ???

  override def update(email: Email, account: Account)(implicit ec: ExecutionContext) =
    ???
}
