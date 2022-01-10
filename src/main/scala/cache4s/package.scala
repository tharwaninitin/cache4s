import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}

package object cache4s {
  case class Entry[T](value: T, ttl: Option[Duration])
  val default_ttl: FiniteDuration = (24 * 60).minutes
}
