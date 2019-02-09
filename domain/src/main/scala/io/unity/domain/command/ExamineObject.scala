package io.unity.domain.command

import io.unity.domain.model.ObjectId

/**
  * A command from a player to have an object examined.
  * @author Sino
  */
case class ExamineObject(objectId: ObjectId)
