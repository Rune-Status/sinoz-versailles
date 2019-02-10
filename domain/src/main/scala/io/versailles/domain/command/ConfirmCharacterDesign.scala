package io.versailles.domain.command

import io.versailles.domain.model.Appearance.{Colours, Sex, Styles}

/**
  * A command that denotes a user wants to confirm a character design.
  * @author Sino
  */
case class ConfirmCharacterDesign(sex: Sex.Type, styles: Styles, colours: Colours)
