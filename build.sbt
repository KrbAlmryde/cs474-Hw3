import sbt.Keys._

val scalactic = "org.scalactic" %% "scalactic" % "3.0.0"
val scalatest = "org.scalatest" %% "scalatest" % "3.0.0" % "test"
val actor =  "com.typesafe.akka" %% "akka-actor" % "2.4.11"
val http = "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11"
val testkit = "com.typesafe.akka" %% "akka-testkit" % "2.4.11"
val json4s = "org.json4s" %% "json4s-native" % "3.3.0"
val jgraph = "org.jgrapht" % "jgrapht-core" % "1.0.0"

//val buhtig = "net.caoticode.buhtig" %% "buhtig" % "0.3.1"


lazy val root = (project in file("."))
    .settings(
        name := "Hw3",
        version := "1.0",
        scalaVersion := "2.11.8",

        // For ScalaTest, disables the buffered Output offered by sbt and uses its own method
        logBuffered in Test := false,
        libraryDependencies ++= Seq(
            scalactic,
            scalatest,
            testkit,
            actor,
            http,
            json4s,
            jgraph
        )
    )