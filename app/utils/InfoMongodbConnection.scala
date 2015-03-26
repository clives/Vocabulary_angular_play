package utils
import play.api.Logger
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.api.Play.current
import play.api.mvc._
import play.api.libs.functional.syntax._
import play.api.libs.json._
import scala.concurrent.Future
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson._
import java.io.{File, InputStream }
import reactivemongo.api.gridfs.DefaultFileToSave
import reactivemongo.api.gridfs.ReadFile
import reactivemongo.api.gridfs.Implicits._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import play.api.Logger
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.api.Play.current
import scala.concurrent._
import ExecutionContext.Implicits.global
/*
 * get info for mongodb, first check if it's working
 */
object InfoMongodbConnection {
	 lazy val db = ReactiveMongoPlugin.db
	 
	 lazy val (connected,errorMessage)={
	   try{
	   	val response=db.collection("fs.chunks").stats()
	   	val result=Await.result( response, 620 seconds)
	   	Logger.debug("Mongodb connected");
	   	(true,None)
	   } catch {
	   	case e: Exception => 
	   	  e.printStackTrace()
	   	  Logger.debug("Mongodb --NOT-- connected");
	   	  (false, Some(e.getMessage()))
	   }
	 }
}