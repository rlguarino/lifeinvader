name := """lifeinvader"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  cache,
  javaWs,
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "org.postgresql" % "postgresql" % "9.3-1101-jdbc41"
)

