name := """gundam-tryage-list"""

version := "1.0"

//resolvers ++= Seq(
//  "Millhouse Bintray"  at "http://dl.bintray.com/themillhousegroup/maven"
//)


scalaVersion := "2.12.10"

libraryDependencies ++= List(
 // "org.apache.httpcomponents.client5" % "httpclient5" % "5.0",
  "org.apache.poi" % "poi" % "4.1.2",
  "org.apache.poi" % "poi-ooxml" % "4.1.2",
  "info.henix" %% "ssoup" % "0.5",
  "commons-io" % "commons-io" % "2.7",
  "com.typesafe" % "config" % "1.4.0",
  "org.typelevel" %% "cats-core" % "1.0.1",
  "org.scalatest" %% "scalatest" % "3.0.5" % Test)

val http4sVersion = "1.0.0-M0+385-ae5377f0-SNAPSHOT"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)


scalacOptions += "-Ypartial-unification"
