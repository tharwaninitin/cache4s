import scala.concurrent.duration.Duration

package object cache4s {
  case class Entry[T](value: T, ttl: Option[Duration])
}
