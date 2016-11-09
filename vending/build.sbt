name := "Vending Machine"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation")

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % "test"

testOptions in Test += Tests.Argument("-oT")
