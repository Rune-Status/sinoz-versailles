package io.versailles.domain.command

import io.versailles.domain.model.ObjectId

/**
  * A command from a player to have an object examined.
  * @author Sino
  */
case class ExamineObject(objectId: ObjectId)
