package io.versailles.domain.model

/**
  * A type of position available for interfaces to be placed on
  * across the client's user interface.
  * @author Sino
  */
object InterfacePosition {
  case object Modal extends Type
  case object Overlay extends Type

  case object PrivateChatOverlay extends Type
  case object Chatbox extends Type
  case object Dialogue extends Type
  case object DataOrbs extends Type
  case object ExpTracker extends Type

  case object AttackStylesTab extends SidebarTab
  case object SkillsTab extends SidebarTab
  case object JourneyBookTab extends SidebarTab
  case object ItemBagTab extends SidebarTab
  case object PrayerTab extends SidebarTab
  case object SpellBookTab extends SidebarTab
  case object ClanChatTab extends SidebarTab
  case object FriendsListTab extends SidebarTab
  case object SubscriptionTab extends SidebarTab
  case object LogoutTab extends SidebarTab
  case object EmotesTab extends SidebarTab
  case object SettingsTab extends SidebarTab
  case object MusicListTab extends SidebarTab

  sealed abstract class SidebarTab extends Type
  sealed abstract class Type
}
