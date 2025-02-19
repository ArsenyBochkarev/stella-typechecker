ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.5"

lazy val root = (project in file("."))
  .settings(
    name := "untitled"
  )

libraryDependencies ++= Seq(
  "org.antlr" % "antlr4" % "4.13.2",
  "org.antlr" % "antlr4-runtime" % "4.13.2"
)