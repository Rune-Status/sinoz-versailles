package io.versailles.domain.model

/**
  * Describes an object.
  * @author Sino
  */
case class ObjectDescriptor(
  id: ObjectId,
  name: ObjectName,
  examine: ObjectTextDescription
)
