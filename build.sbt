val commonSettings = Seq(
  version := "0.1",
  organization := "unity",
  scalaVersion := "2.12.8"
)

val twitterUtilVersion = "19.1.0"
val zioVersion = "0.6.0"
val nettyVersion = "4.1.33.Final"
val scalaTestVersion = "3.0.5"

lazy val domain = project("domain")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= {
      val main = Seq(
        "io.netty" % "netty-common" % nettyVersion,
        "org.scalaz" %% "scalaz-zio" % zioVersion,
        "com.twitter" %% "util-collection" % twitterUtilVersion
      )
      
      val test = Seq(
        "org.scalatest" %% "scalatest" % scalaTestVersion % Test
      )
      
      main ++ test
    }
  )

lazy val application = project("application")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= {
      val main = Seq(
        "org.scalaz" %% "scalaz-zio" % zioVersion,
        "io.netty" % "netty-buffer" % nettyVersion,
        "com.twitter" %% "util-collection" % twitterUtilVersion
      )
      
      val test = Seq(
        "org.scalatest" %% "scalatest" % scalaTestVersion % Test
      )
      
      main ++ test
    }
  )
  .dependsOn(domain)

def project(id: String) = Project(id, base = file(id))