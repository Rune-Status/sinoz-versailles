package io.unity.application.component.account

import io.unity.domain.model.{Account, AccountRepository, Email}

/**
  * A service that deals with [[Account]]s.
  * @author Sino
  */
final class AccountService(repository: AccountRepository) {
  def get(email: Email) = repository.get(email)
  def update(email: Email, account: Account) = repository.update(email, account)
}
