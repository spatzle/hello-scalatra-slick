import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object HelloscalatraBuild extends Build {
  val Organization = "org.smartjava"
  //  val Organization = "com.typesafe.slick.examples.jdbc"
  val Name = "hello-scalatra"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.10.0"
  val ScalatraVersion = "2.2.0"

  lazy val project = Project(
    "hello-scalatra",
    file("."),
    settings = Defaults.defaultSettings ++ ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.0.6" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container",
        "net.liftweb" %% "lift-json" % "[2.4,)",
        "com.typesafe.slick" %% "slick" % "1.0.0",
        "com.h2database" % "h2" % "1.3.166",
        "c3p0" % "c3p0" % "0.9.1.2",
        //        "org.scalaquery" %% "scalaquery" % "0.10.0-M1",
        "postgresql" % "postgresql" % "9.1-901.jdbc4",
        "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),
        "org.slf4j" % "slf4j-nop" % "1.6.4",
        "com.h2database" % "h2" % "1.3.170",
        "org.xerial" % "sqlite-jdbc" % "3.6.20",
        "mysql" % "mysql-connector-java" % "5.1.13"
//        "org.scalatra" %% "scalatra-json" % "2.2.0",
//        "org.json4s" %% "json4s-jackson" % "3.1.0"
        ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile) { base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty, /* default imports should be added here */
            Seq.empty, /* add extra bindings here */
            Some("templates")))
      }))
}
