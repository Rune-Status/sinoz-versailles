package io.unity.domain.model

/**
  * A mode that defines when to stack [[Item]]s in an [[Inventory]].
  * @author Sino
  */
object StackMode {
  case object AlwaysStack extends Type
  case object StackablesOnly extends Type
  case object NeverStack extends Type
  sealed abstract class Type
}
