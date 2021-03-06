organization:= "com.github.edvakf"

name := "play-requestcount"

version := "0.0.4"

scalaVersion := "2.12.8"

crossScalaVersions ++= Seq("2.11.12", "2.12.8")

scalacOptions ++= Seq("-deprecation")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play" % "2.6.3" % "provided",
    "com.typesafe.play" %% "play-test" % "2.6.3" % "test",
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
)
