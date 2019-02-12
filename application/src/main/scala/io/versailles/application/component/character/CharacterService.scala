package io.versailles.application.component.character

import io.versailles.domain.model.{CharacterProfile, CharacterRepository, Email}

import scala.async.Async.{async, await}
import scala.concurrent.duration.Duration
import scala.concurrent.{ExecutionContext, Future}

/**
  * A service that deals with [[CharacterProfile]]s.
  * @author Sino
  */
final class CharacterService(permanentStorage: CharacterRepository, cache: CharacterCache, expireAfter: Duration) {
  /** Fetches a [[CharacterProfile]] from the cache. If no [[CharacterProfile]]
    * exists under the specified [[Email]], an attempt is made to fetch from
    * the [[permanentStorage]] instead. */
  def getCharacter(email: Email)(implicit ec: ExecutionContext): Future[Option[CharacterProfile]] =
    async {
      val profileOpt = await(cache.get(email))
      if (profileOpt.isEmpty) {
        await(permanentStorage.get(email))
      } else {
        profileOpt
      }
    }

  /** Saves the given [[CharacterProfile]] under the specified [[Email]]. The
    * profile is saved in both the permanent storage and the cache. This is done
    * in parallel. Failure from either operation is escalated into the returned
    * [[Future]]. */
  def saveCharacter(email: Email, profile: CharacterProfile)(implicit ec: ExecutionContext): Future[Boolean] =
    async {
      val storedPermanently = permanentStorage.update(email, profile)
      val cached = cache.put(email, profile, expireAfter)

      val f1 = await(storedPermanently)
      val f2 = await(cached)

      f1 && f2
    }
}
