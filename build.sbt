import Dependencies._
import ScalaCompileOptions._
import Versions._

lazy val cache4s = (project in file("."))
  .settings(
    name         := "cache4s",
    version      := "0.1.0",
    scalaVersion := scala212,
    libraryDependencies ++= core ++ testLibs,
    crossScalaVersions := allScalaVersions,
    scalacOptions ++= {
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, _)) => s2copts
        case Some((3, _)) => s3copts
        case _            => Seq()
      }
    },
    Test / parallelExecution := false,
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
