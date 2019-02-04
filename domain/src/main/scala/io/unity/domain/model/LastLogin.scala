package io.unity.domain.model

import java.time.LocalDate

/**
  * The date an account was last logged into.
  * @author Sino
  */
case class LastLogin(private val value: LocalDate) extends AnyVal {
  def daysSince(other: LocalDate) = value.until(other).getDays
}
