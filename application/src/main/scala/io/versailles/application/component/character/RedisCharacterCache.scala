package io.versailles.application.component.character

import io.versailles.domain.model.{CharacterProfile, Email}
import redis.RedisClient

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.Duration

/**
  * A [[CharacterCache]] that stores [[CharacterProfile]]s in a local or remote
  * Redis server.
  * @author Sino
  */
final class RedisCharacterCache(redisClient: RedisClient) extends CharacterCache {
  override def get(email: Email)(implicit ec: ExecutionContext) =
    ???

  override def put(email: Email, profile: CharacterProfile, expireAfter: Duration)(implicit ec: ExecutionContext) =
    ???
}
