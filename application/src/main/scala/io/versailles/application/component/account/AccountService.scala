package io.versailles.application.component.account

import io.versailles.domain.model.{Account, AccountRepository, Email}

import scala.concurrent.ExecutionContext

/**
  * A service that deals with [[Account]]s.
  * @author Sino
  */
final class AccountService(repository: AccountRepository) {
  /** Looks up an [[Account]] under the specified [[Email]]. */
  def getAccount(email: Email)(implicit ec: ExecutionContext) =
    repository.get(email)

  /** Saves the given [[Account]] under the specified [[Email]]. */
  def saveAccount(email: Email, account: Account)(implicit ec: ExecutionContext) =
    repository.update(email, account)
}
