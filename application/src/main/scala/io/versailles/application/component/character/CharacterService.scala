package io.versailles.application.component.character

import io.versailles.domain.model.{CharacterRepository, Email}

/**
  * A service that deals with the manipulation of
  * [[io.versailles.domain.model.CharacterProfile]]s.
  * @author Sino
  */
final class CharacterService(permanent: CharacterRepository, cache: CharacterCache) {
  def getFromCache(email: Email) =
    for { result  <- cache.get(email) } yield result

  def getFromPermStorage(email: Email) =
    for { result <- permanent.get(email) } yield result
}
