package io.versailles.domain.model

import java.util.regex.Pattern

object Email {
  /**
    * These are the rules for the password [[Pattern]] described below:
    *
    * ^                 =>  start-of-string
    * $                 =>  end-of-string
    */
  val ValidEmailAddressPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
}

/**
  * An e-mail address.
  * @author Sino
  */
case class Email(private val value: String) extends AnyVal {
  def isValid = Email.ValidEmailAddressPattern.matcher(value).find()

  def toValue = value
}