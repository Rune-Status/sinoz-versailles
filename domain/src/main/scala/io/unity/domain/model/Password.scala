package io.unity.domain.model

import java.util.regex.Pattern

object Password {
  /**
    * These are the rules for the password [[Pattern]] described below:
    *
    * ^                 =>  start-of-string
    * (?=.*[0-9])       =>  a digit must occur at least once
    * (?=.*[a-z])       =>  a lower case letter must occur at least once
    * (?=.*[A-Z])       =>  an upper case letter must occur at least once
    * (?=.*[@#$%^&+=])  =>  a special character must occur at least once
    * (?=\S+$)          =>  no whitespace allowed in the entire string
    * .{6,}             =>  anything, at least six places though
    * $                 =>  end-of-string
    */
  val ValidPasswordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$")
}

/**
  * A password of an [[Account]].
  * @author Sino
  */
case class Password(private val value: String) extends AnyVal {
  def isValid = Password.ValidPasswordPattern.matcher(value).find()

  def toValue = value
}
