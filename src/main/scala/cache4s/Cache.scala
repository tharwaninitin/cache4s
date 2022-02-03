package cache4s

import com.github.benmanes.caffeine.cache.{Cache => CCache, Caffeine, Expiry}
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters._
import scala.util.Try

case class Cache[K, V](underlying: CCache[K, Entry[V]]) extends Service[K, V] {
  override def get(key: K): Option[V] = Try(underlying.getIfPresent(key).value).toOption

  override def remove(key: K): Unit = underlying.invalidate(key)

  override def put(key: K, value: V, ttl: Option[Duration]): Unit = underlying.put(key, Entry(value, ttl))

  override def toMap: Map[K, V] = underlying.asMap().asScala.map(x => (x._1, x._2.value)).toMap

  override def getValues: List[V] = underlying.asMap().values.asScala.map(_.value).toList

  override def getStats: Map[String, String] =
    Map(
      "hitCount"     -> underlying.stats.hitCount().toString,
      "hitRate"      -> underlying.stats.hitRate().toString,
      "size"         -> underlying.estimatedSize().toString,
      "missCount"    -> underlying.stats.missCount().toString,
      "missRate"     -> underlying.stats.missRate().toString,
      "requestCount" -> underlying.stats.requestCount().toString
    )
}

object Cache {
  def defaultExpirationPolicy[K, V]: Expiry[K, Entry[V]] = new Expiry[K, Entry[V]]() {
    override def expireAfterCreate(key: K, value: Entry[V], currentTime: Long): Long =
      value.ttl.map(_.toNanos).getOrElse(Long.MaxValue)
    override def expireAfterUpdate(key: K, value: Entry[V], currentTime: Long, currentDuration: Long): Long =
      currentDuration
    override def expireAfterRead(key: K, value: Entry[V], currentTime: Long, currentDuration: Long): Long =
      currentDuration
  }

  def create[K, V](
      maximumSize: Long = 1000L,
      expirationPolicy: Expiry[K, Entry[V]] = defaultExpirationPolicy[K, V]
  ): Cache[K, V] = {

    val cache: CCache[K, Entry[V]] =
      Caffeine
        .newBuilder()
        .asInstanceOf[Caffeine[K, Entry[V]]]
        .recordStats()
        .expireAfter(expirationPolicy)
        .maximumSize(maximumSize)
        .build[K, Entry[V]]()

    Cache[K, V](cache)
  }
}
