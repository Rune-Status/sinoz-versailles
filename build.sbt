val commonSettings = Seq(
  version := "0.1",
  organization := "unity",
  scalaVersion := "2.12.8"
)

val catsVersion = "1.2.0"
val catsDeps = Seq(
  "org.typelevel" %% "cats-effect" % catsVersion
)

val utilDeps = Seq(
  "com.twitter" %% "util-collection" % "19.1.0"
)

val testDeps = Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

val nettyVersion = "4.1.33.Final"
val nettyDeps = Seq(
  "io.netty" % "netty-buffer" % nettyVersion,
  "io.netty" % "netty-common" % nettyVersion
)

lazy val domain = project("domain")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= {
      catsDeps ++ utilDeps ++ testDeps
    }
  )

lazy val application = project("application")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= {
      catsDeps ++ nettyDeps ++ utilDeps ++ testDeps
    }
  )
  .dependsOn(domain)

def project(id: String) = Project(id, base = file(id))