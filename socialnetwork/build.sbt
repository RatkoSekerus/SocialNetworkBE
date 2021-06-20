name := """SocialNetwork"""
organization := "novaLite"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.5"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0"
)

libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.22"

libraryDependencies += "com.pauldijou" %% "jwt-play-json" % "4.3.0"
libraryDependencies += "com.pauldijou" %% "jwt-play" % "4.3.0"
libraryDependencies += "com.pauldijou" %% "jwt-core" % "4.3.0"
libraryDependencies += "com.auth0" % "jwks-rsa" % "0.6.1"
libraryDependencies ++= Seq("io.jsonwebtoken" % "jjwt" % "0.9.1")
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "novaLite.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "novaLite.binders._"
