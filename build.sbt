name := """managementstudentcourseapp"""
organization := "com.oktanachallenge"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.2" % Test

libraryDependencies ++= Seq(
  ws,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.12.7-play26",
  "io.swagger"        %% "swagger-play2"       % "1.7.1",
  "org.webjars"       %  "swagger-ui"          % "3.2.2"
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.oktanachallenge.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.oktanachallenge.binders._"


play.sbt.routes.RoutesKeys.routesImport += "play.modules.reactivemongo.PathBindables._"
