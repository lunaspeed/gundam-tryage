name := """gundam-tryage-list"""

version := "1.0"

//resolvers ++= Seq(
//  "Millhouse Bintray"  at "http://dl.bintray.com/themillhousegroup/maven"
//)


scalaVersion := "2.12.6"

libraryDependencies ++= List(
  "org.apache.httpcomponents" % "httpclient" % "4.5.6",
  "org.apache.poi" % "poi" % "4.0.0",
  "org.apache.poi" % "poi-ooxml" % "4.0.0",
  "info.henix" %% "ssoup" % "0.5",
  "commons-io" % "commons-io" % "2.6",
  "com.typesafe" % "config" % "1.3.3",
  "org.typelevel" %% "cats-core" % "1.0.1")



scalacOptions += "-Ypartial-unification"
