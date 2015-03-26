package controllers

import scala.concurrent.Future

import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.gridfs.GridFS
import reactivemongo.api.gridfs.Implicits.DefaultReadFileReader
import reactivemongo.api.gridfs.ReadFile
import reactivemongo.bson.{BSONDocument,BSONDocumentIdentity,BSONObjectID,BSONObjectIDIdentity,BSONValue}
import reactivemongo.bson.Producer.nameValue2Producer


/*
 */
object ApplicationAudioFile extends Controller with MongoController {
  
  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
  def collection: JSONCollection = db.collection[JSONCollection]("audiofile")
  


	val gridFS = new GridFS(db, "fs") 

	/*
	 * upload file
	 * 
	 * upload file to mongodb. if the body contains an audioId=> the user change the
	 * audio file before saving the vocabulary => we have to remove the older one.
	 * 
	 * @return json with url to consume the file and is id
	 * 
	 * */
	def upload = Action.async(gridFSBodyParser(gridFS)){ request =>
		// here is the future file!
		val futureFile: Future[ReadFile[BSONValue]] = request.body.files.head.ref
		val getPathToFile=routes.ApplicationAudioFile.getAudioFile _
		
		//if none => creating a new audio file, otherwise
		//we have to remove the older one
		val audioId= request.body.dataParts.get("audioId").map( _.head );
	
	  println("audioId:"+audioId);
		
		audioId match{
		  case Some(idfile) =>
		    println("delete id");
		    deleteAudioFile(idfile)
		  case _=> println("no id");
		}
		
		val futurresult=futureFile.map { file =>
		  file.id
		  val idfile=file.id.asInstanceOf[BSONObjectID].stringify
			// we have the id file , file.id	
		  println("ok:"+idfile);
		  println("success, url:"+getPathToFile( idfile));
		  
			Ok(s"""{ "url": "${getPathToFile( idfile)}", "audioId":"${idfile}" }""")
		}.recover{
			case e: Throwable => InternalServerError(e.getMessage)
		}
		futurresult
	}

	
	
	/****************************************************************************
	 * get report file as html using id
	 ***************************************************************************/
	def getAudioFile(id: String) =Action.async { request =>
			val file = gridFS.find(BSONDocument("_id" -> BSONObjectID(id)))
			
			Logger.info(s"result find id:$id")
			request.getQueryString("inline") match {
			//	case Some("true") => serve(gridFS, file, CONTENT_DISPOSITION_INLINE)
				case _            => serve(gridFS, file, CONTENT_DISPOSITION_INLINE)
			}
	}

	
	/*
	 * delete an audiofile
	 */
	def deleteAudioFile( idfile: String) ={
	  import play.modules.reactivemongo.json.BSONFormats._
	  
    val futureRemove = gridFS.remove(BSONObjectID(idfile))
	  futureRemove.map { file =>
	    println(s"delete ok $idfile , "+file);
		  
		}.recover{
			case e: Throwable =>
			  println(e);
		}
	}

}