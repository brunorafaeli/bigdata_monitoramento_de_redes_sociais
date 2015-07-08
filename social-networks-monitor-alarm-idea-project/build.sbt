name := "social-networks-monitor-alarm-idea-project"

version := "1.0"

scalaVersion := "2.10.4"

scalacOptions += "-target:jvm-1.7"
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.3.1"

libraryDependencies += "org.apache.spark" %% "spark-streaming-twitter" % "1.4.0"

libraryDependencies += "com.googlecode.json-simple" % "json-simple" % "1.1.1"

mainClass in assembly := some("snMonitor")
assemblyJarName := "social-networks-monitor-alarm-idea-project.jar"

val meta = """META.INF(.)*""".r
assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case n if n.endsWith(".conf") => MergeStrategy.concat
  case meta(_) => MergeStrategy.discard
  case x => MergeStrategy.first
}