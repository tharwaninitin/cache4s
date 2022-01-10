package cache4s

import scala.concurrent.duration.Duration

trait Service[K, V] {
  def get(key: K): Option[V]
  def remove(key: K): Unit
  def put(key: K, value: V, ttl: Option[Duration] = None): Unit
  def toMap: Map[String, String]
  def getValues: List[V]
  def getStats: Map[String, String]
}
