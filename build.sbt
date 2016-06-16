organization:= "com.github.edvakf"

name := "play-requestcount"

version := "0.0.2"

scalaVersion := "2.11.6"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play" % "2.5.4" % "provided",
    "com.typesafe.play" %% "play-test" % "2.5.4" % "test",
    "org.scalatest" %% "scalatest" % "2.1.5" % "test"
)
