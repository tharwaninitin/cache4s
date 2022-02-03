package cache4s

import scala.concurrent.duration.Duration

trait Service[K, V] {
  def get(key: K): Option[V]
  def put(key: K, value: V, ttl: Option[Duration] = None): Unit
  def remove(key: K): Unit
  def toMap: Map[K, V]
  def getValues: List[V]
  def getStats: Map[String, String]
}
