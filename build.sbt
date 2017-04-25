import NativePackagerKeys._

herokuAppName in Compile := "protected-gorge-44302"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

name := """pvt"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  "mysql" % "mysql-connector-java" % "5.1.34",
  cache,
  javaWs,
  evolutions,
  filters
)

routesGenerator := InjectedRoutesGenerator

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"
javaSource in Test := baseDirectory.value / "test"
