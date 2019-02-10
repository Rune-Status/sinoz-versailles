val commonSettings = Seq(
  version := "0.1",
  organization := "versailles",
  scalaVersion := "2.12.8"
)

val typesafeConfigVersion = "1.3.3"
val twitterUtilVersion = "19.1.0"

val logbackVersion = "1.2.3"

val zioVersion = "0.6.0"
val zioInteropVersion = "0.5.0"

val nettyVersion = "4.1.33.Final"
val scalaTestVersion = "3.0.5"

val redisClientVersion = "3.9"
val bcryptLibVersion = "0.4"

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
        "ch.qos.logback" % "logback-classic" % logbackVersion,
        
        "net.debasishg" %% "redisclient" % redisClientVersion,
        
        "org.scalaz" %% "scalaz-zio" % zioVersion,
        "org.scalaz" %% "scalaz-zio-interop" % zioInteropVersion,

        "io.netty" % "netty-all" % nettyVersion,
        
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