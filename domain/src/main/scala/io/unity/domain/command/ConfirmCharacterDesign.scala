package io.unity.domain.command

import io.unity.domain.model.Appearance.{Colours, Sex, Styles}

/**
  * A command that denotes a user wants to confirm a character design.
  * @author Sino
  */
case class ConfirmCharacterDesign(sex: Sex.Type, styles: Styles, colours: Colours)
