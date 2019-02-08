name := """gundam-tryage-list"""

version := "1.0"

//resolvers ++= Seq(
//  "Millhouse Bintray"  at "http://dl.bintray.com/themillhousegroup/maven"
//)


scalaVersion := "2.12.7"
val poiVersion = "4.0.1"
val log4j2Version = "2.11.1"

libraryDependencies ++= List(
  "org.apache.httpcomponents" % "httpclient" % "4.5.7",
  "org.apache.poi" % "poi" % poiVersion,
  "org.apache.poi" % "poi-ooxml" % poiVersion,
  "info.henix" %% "ssoup" % "0.5",
  "commons-io" % "commons-io" % "2.6",
  "com.typesafe" % "config" % "1.3.3",
  "org.typelevel" %% "cats-core" % "1.6.0",
  "org.apache.logging.log4j" % "log4j-api" % log4j2Version,
  "org.apache.logging.log4j" % "log4j-core" % log4j2Version,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4j2Version,
  "org.apache.logging.log4j" %% "log4j-api-scala" % "11.0",
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.3.0",
  "mysql" % "mysql-connector-java" % "8.0.15")




scalacOptions += "-Ypartial-unification"
