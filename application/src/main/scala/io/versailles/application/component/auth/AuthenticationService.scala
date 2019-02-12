package io.versailles.application.component.auth

import io.versailles.application.component.account.AccountService
import io.versailles.application.model.PinCodeInput
import io.versailles.domain.event.{CouldNotFindAccount, PasswordsMismatched}
import io.versailles.domain.model.{Email, Password}

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext

/**
  * A service in charge of authenticating user accounts.
  * @author Sino
  */
final class AuthenticationService(accountService: AccountService, passwordMatcher: PasswordMatcher) {
  def authenticate(email: Email, password: Password, pinCodeInput: Option[PinCodeInput.Type])(implicit ec: ExecutionContext) =
    async {
      val account = await(firstStepAuth(email, password))

      // TODO second step auth if enabled

      account
    }

  private def firstStepAuth(email: Email, password: Password)(implicit ec: ExecutionContext) =
    async {
      val accountOpt = await(accountService.getAccount(email))
      if (accountOpt.isEmpty) {
        throw CouldNotFindAccount
      }

      val passwordsMatch = passwordMatcher.matched(plain = password, hashed = accountOpt.get.password)
      if (!passwordsMatch) {
        throw PasswordsMismatched
      }

      accountOpt.get
    }
}
