val commonSettings = Seq(
  version := "0.1",
  organization := "versailles",
  scalaVersion := "2.12.8"
)

val typesafeConfigVersion = "1.3.3"
val twitterUtilVersion = "19.1.0"

val akkaVersion = "2.5.20"

val logbackVersion = "1.2.3"

val nettyVersion = "4.1.33.Final"
val scalaTestVersion = "3.0.5"

val redisClientVersion = "1.8.0"
val bcryptLibVersion = "0.4"

lazy val domain = project("domain")
  .settings(commonSettings: _*)
  .settings(
    libraryDependencies ++= {
      val main = Seq(
        "io.netty" % "netty-common" % nettyVersion,
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
        "com.typesafe.akka" %% "akka-actor" % akkaVersion,
        "com.typesafe.akka" %% "akka-testkit" % akkaVersion,

        "com.github.etaty" %% "rediscala" % redisClientVersion,
        
        "ch.qos.logback" % "logback-classic" % logbackVersion,

        "io.netty" % "netty-buffer" % nettyVersion,
        
        "com.typesafe" % "config" % typesafeConfigVersion,
        "com.twitter" %% "util-collection" % twitterUtilVersion,

        "org.mindrot" % "jbcrypt" % bcryptLibVersion
      )
      
      val test = Seq(
        "org.scalatest" %% "scalatest" % scalaTestVersion % Test
      )
      
      main ++ test
    }
  )
  .dependsOn(domain)

def project(id: String) = Project(id, base = file(id))