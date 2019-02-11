package io.versailles.domain.model

import io.netty.util.{AttributeKey, DefaultAttributeMap}

object Item {
  def apply(descriptor: ItemDescriptor, quantity: Int = 1): Item =
    Item(descriptor, quantity, attributes = new DefaultAttributeMap)
}

/**
  * An item entity.
  * @author Sino
  */
case class Item(descriptor: ItemDescriptor, quantity: Int, private val attributes: DefaultAttributeMap) {
  /** Attaches the given value to the given [[AttributeKey]] and stores it as
    * an attribute of this item. */
  def withAttribute[V](key: AttributeKey[V], value: V) = {
    attributes.attr[V](key).set(value)
    this
  }

  /** Checks if a value is attached to the given [[AttributeKey]]. */
  def containsAttribute[V](key: AttributeKey[V]): Boolean =
    attributeOf(key).isDefined

  /** Looks up the value that is attached to the given [[AttributeKey]]. */
  def attributeOf[V](key: AttributeKey[V]): Option[V] =
    Option(attributes.attr[V](key).get())

  /** Looks up the value that is attached to the given [[AttributeKey]] and
    * detaches the value from the key, removing it from the set of item
    * attributes. */
  def withoutAttribute[V](key: AttributeKey[V]) = {
    attributes.attr[V](key).set(null.asInstanceOf[V])
    this
  }
}
