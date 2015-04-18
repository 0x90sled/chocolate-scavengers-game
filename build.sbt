name := "chocolate-scavengers-game"

description := "Roguelike 2d game for the Chocolate game engine. Based on Unity tutorial"

scalaVersion := "2.11.1"

///////////////////////////////////////////////////////////////////////////////////////////////////

lazy val chocolateGameExamples = FDProject(
	"org.uqbar" %% "chocolate-core" % "[1.0.0-SNAPSHOT)",
	"org.scalatest" %% "scalatest" % "[2.2,)" % "test"
)

///////////////////////////////////////////////////////////////////////////////////////////////////

unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value)

unmanagedSourceDirectories in Test := Seq((scalaSource in Test).value)

scalacOptions += "-feature"
