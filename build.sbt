
name := """FR_BR_Voc"""

version := "1.0-SNAPSHOT"

//JsEngineKeys.engineType := JsEngineKeys.EngineType.CommonNode

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.3-M1",
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "requirejs" % "2.1.11-1",
  "org.webjars" % "emberjs" % "1.5.0",
  "org.scalatest" % "scalatest_2.10" % "2.1.3" % "test",
  "com.typesafe.akka" % "akka-testkit_2.10" % "2.3.1" % "test",
  "org.webjars" % "handlebars" % "1.1.2",
  "org.webjars" % "emberjs-data" % "1.0.1-beta.11" 
)

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.0-SNAPSHOT-2.3-M1" exclude("com.typesafe.play", "play-iteratees_2.10") //intransitive()
)

//needed for scalatestplus
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-ws" % "2.3-M1"
)

libraryDependencies += "org.scalatestplus" %% "play" % "1.0.0" % "test"

play.PlayImport.PlayKeys.playDefaultPort := 9001

lazy val root = (project in file(".")).addPlugins(PlayScala)

initialCommands in console := """new play.core.StaticApplication(new java.io.File("."));
import _root_.dao._;import play.api.libs.concurrent.Execution.Implicits.defaultContext;import models._;"""
//import models._;
//new play.core.StaticApplication(new java.io.File("."))
//val dao=new DAOVocabularyMongodb()
//val voc1= new Vocabulary("test", "test", "lalala", None, None);
//"""