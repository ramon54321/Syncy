name := "syncy"

version := "1.0"

scalaVersion := "2.12.5"

mainClass in (Compile, packageBin) := Some("syncy.Main")

mainClass in (Compile, run) := Some("syncy.Main")

libraryDependencies ++=  Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.11",
  "com.typesafe.akka" %% "akka-remote" % "2.5.11",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test
)
