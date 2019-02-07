package io.unity.application.component.auth

import io.unity.application.component.account.AccountService

/**
  * A service that is in charge of authenticating users logging into
  * an [[io.unity.domain.model.Account]]
  * @author Sino
  */
final class AuthenticationService(accountService: AccountService, passwordMatcher: PasswordMatcher) {
  // TODO
}
