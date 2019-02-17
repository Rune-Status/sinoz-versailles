package io.versailles.domain.model

import io.netty.util.AttributeKey

object Item {
  def apply(descriptor: ItemDescriptor, quantity: Int = 1): Item =
    Item(descriptor, quantity, attributes = Map.empty[AttributeKey[_ <: Any], Any])
}

/**
  * A physical and potentially stackable game item entity that may
  * further have attributes to describe its current state.
  * @author Sino
  */
case class Item(descriptor: ItemDescriptor, quantity: Int, private val attributes: Map[AttributeKey[_ <: Any], Any]) {
  /** Attaches the given value to the given [[AttributeKey]] and stores it as
    * an attribute of this item. */
  def withAttribute[V](key: AttributeKey[V], value: V) =
    copy(attributes = attributes + (key -> value))

  /** Checks if a value is attached to the given [[AttributeKey]]. */
  def containsAttribute[V](key: AttributeKey[V]): Boolean =
    attributeOf(key).isDefined

  /** Looks up the value that is attached to the given [[AttributeKey]]. */
  def attributeOf[V](key: AttributeKey[V]) =
    attributes.get(key).map(_.asInstanceOf[V])

  /** Looks up the value that is attached to the given [[AttributeKey]] and
    * detaches the value from the key, removing it from the set of item
    * attributes. */
  def withoutAttribute[V](key: AttributeKey[V]) =
    copy(attributes = attributes - key)
}
