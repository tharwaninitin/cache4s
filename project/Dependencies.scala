import Versions._
import sbt._

object Dependencies {
  lazy val core = List(
    "com.github.ben-manes.caffeine" % "caffeine"                % CaffeineCacheVersion,
    "org.scala-lang.modules"       %% "scala-collection-compat" % ScalaJdkConversionVersion
  )
  lazy val testLibs = List(
    "dev.zio" %% "zio-test"     % ZioVersion,
    "dev.zio" %% "zio-test-sbt" % ZioVersion
  ).map(_ % Test)
}
