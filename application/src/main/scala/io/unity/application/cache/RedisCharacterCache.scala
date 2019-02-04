package io.unity.application.cache

import io.unity.domain.model.{CharacterProfile, Email}

import scala.concurrent.duration.Duration

/**
  * A [[CharacterCache]] that stores [[CharacterProfile]]s in a local or remote
  * Redis server.
  * @author Sino
  */
final class RedisCharacterCache extends CharacterCache {
  override def get(email: Email) = ???
  override def put(email: Email, profile: CharacterProfile, expireAfter: Duration) = ???
}
