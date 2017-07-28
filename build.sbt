name := "spark-test-csv"

version := "1.0"

scalaVersion := "2.11.8"


resolvers += "Maven Central" at "http://central.maven.org/maven2/"

libraryDependencies ++= Seq(
   "org.apache.spark" % "spark-sql_2.11" % "2.1.1" % "provided"
  ,"com.typesafe" % "config" % "1.3.1"
  ,"com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
  ,"org.json4s" % "json4s-jackson_2.11" % "3.5.2"
  ,"org.scalatest" % "scalatest_2.11" % "3.0.3" % "test"
)

parallelExecution in Test := false

// Turning off cross-building between multiple versions of scala. So that the output goes under target.
// Turn this on if intend to compile against multiple versions of scala, i.e. crossScalaVersions := Seq("2.8.2", "2.9.2", "2.10.0")
crossPaths := false

assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("org.json4s.**" -> "shadeio.@1").inLibrary("org.json4s" % "json4s-jackson_2.11" % "3.5.2").inProject
)