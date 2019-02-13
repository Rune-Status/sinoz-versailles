package io.versailles.application.component.game

import io.versailles.domain.model.PID
import org.scalatest.{FlatSpec, Matchers}

final class StackfulPIDRepositorySpec extends FlatSpec with Matchers {
  "The repository" should "be able to obtain a PID" in {
    val repository = new StackfulPIDRepository(16)
    val pid = repository.obtain()

    pid should not be empty
  }

  "The repository" should "return PID's starting at 1" in {
    val repository = new StackfulPIDRepository(16)
    val pid = repository.obtain()

    pid should contain(PID(1))
  }

  "The repository" should "be able to release a PID back for reuse" in {
    val repository = new StackfulPIDRepository(16)

    repository.release(PID(17))
    repository.size() should be(17)
  }

  "The repository" should "always take PID's from the top of the stack" in {
    val repository = new StackfulPIDRepository(16)

    repository.release(PID(32))
    repository.obtain() should contain(PID(32))
  }
}
