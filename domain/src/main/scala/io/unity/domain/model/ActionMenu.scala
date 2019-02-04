package io.unity.domain.model

object ActionMenu {
  object Option {
    case object Attack extends Type(slot = 0, label = "Attack")
    case object Challenge extends Type(slot = 0, label = "Challenge")
    case object TradeWith extends Type(slot = 3, label = "Trade with")
    case object Follow extends Type(slot = 4, label = "Follow")
    case object Report extends Type(slot = 8, label = "Report")

    sealed abstract class Type(val slot: Int, val label: String)
  }

  def create = ActionMenu(Vector.tabulate(16)(_ => None))
}

/**
  * A right click menu with options when performed on another player.
  * @author Sino
  */
case class ActionMenu private(options: Vector[Option[ActionMenu.Option.Type]]) extends AnyVal {
  def show(optionType: ActionMenu.Option.Type) = copy(options = options.updated(optionType.slot, Some(optionType)))

  def hide(optionType: ActionMenu.Option.Type) = copy(options = options.updated(optionType.slot, None))
}
