lazy val AkkaVersion = "2.6.20"
lazy val AkkaHttpVersion = "10.2.10"
lazy val AkkaManagementVersion = "1.1.4"
lazy val ScalaTestVersion = "3.2.16"
lazy val LogbackVersion = "1.4.7"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-Xlint",
  "-Xsource:3"
)
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / dynverSeparator := "-"
ThisBuild / Compile / doc / autoAPIMappings := true

lazy val main = (project in file("."))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "skaffold-sbt-example",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-cluster-sharding-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-discovery" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream-typed" % AkkaVersion,
      "com.lightbend.akka.discovery" %% "akka-discovery-kubernetes-api" % AkkaManagementVersion,
      "com.lightbend.akka.management" %% "akka-management" % AkkaManagementVersion,
      "com.lightbend.akka.management" %% "akka-management-cluster-http" % AkkaManagementVersion,
      "com.lightbend.akka.management" %% "akka-management-cluster-bootstrap" % AkkaManagementVersion,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % Test
    ),
    // Docker
    dockerBaseImage := "adoptopenjdk:11-jre-hotspot",
    dockerUpdateLatest := true,
    dockerExposedPorts := Seq(8080, 8558, 25520)
  )
