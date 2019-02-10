package io.versailles.application.component.auth

import io.versailles.application.component.account.AccountService

/**
  * A service that is in charge of authenticating users logging into
  * an [[io.versailles.domain.model.Account]]
  * @author Sino
  */
final class AuthenticationService(accountService: AccountService, passwordMatcher: PasswordMatcher) {
  // TODO
}
