import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "HeyBro",
    libraryDependencies ++= Seq(
      scalaTest % Test,

      "com.softwaremill.sttp" %% "core" % "1.1.13",
      "com.typesafe.play" %% "play-json" % "2.6.7",
      metadataExtractor,
      scalaXml,
      "com.softwaremill.sttp" %% "core" % "1.1.13",
      "com.typesafe.play" %% "play-json" % "2.6.7",
      "com.github.pathikrit" %% "better-files" % "3.4.0",
      "com.typesafe.play" %% "play-json-joda" % "2.6.0-RC1"
    )

)
