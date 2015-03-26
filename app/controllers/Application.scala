package controllers

import play.api.mvc.{Action, Controller}
import play.api.libs.json.Json
import play.api.mvc.BodyParsers
import play.api.libs.json.JsError
import models.Vocabulary
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import VocabularyApplication._
import scala.util.Failure
import scala.util.Success

object Application extends Controller {
  def index = Action { 
    Ok(views.html.index("Hello Play Framework"))
  }
  
  def vocabulary = Action { implicit request =>
    Ok(views.html.vocabulary("Hello Play Framework"))
  }
  
  def listVocabularies = Action.async {
    daoVocabulary.getAll().map{ result=>
    	val json = Json.toJson(result)
    	Ok(json)
    }.recover{
			case e: Throwable => InternalServerError(e.getMessage)
			  
		}
  }
  
 def getVocabulary(id:String)=Action.async { request =>
    val ourmovie=daoVocabulary.get(id)
    ourmovie.map{ _ match{
	      case None =>  BadRequest(Json.obj("status" ->"KO", "message" -> s"missing movie for this id $id"))
	      case Some(movie)=>
	      	val json = Json.toJson(movie)
	      	Ok(json)
    	}
    }
  }  
  
 def deleteVocabulary(id:String) = Action.async { request =>
    daoVocabulary.delete(id).map{  	    
       _=> Ok(Json.obj("status" ->"OK" )) 
    }.recover{
      case error: Throwable =>
        BadRequest(Json.obj("status" ->"KO", "message" -> error.getMessage()))
        
    }       
  }
 
  def updateVocabulary(id:String) = Action(BodyParsers.parse.json) { request =>
    val placeResult = request.body.validate[Vocabulary]
    placeResult.fold(
	    errors => {
	      BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
	    },
	    movie => { 
	      daoVocabulary.update(movie)
	      val json = Json.toJson(movie)
	      Ok(json) 
	    }
	  )    
  } 
  
  def saveVocabulary = Action(BodyParsers.parse.json) { request =>

	  val placeResult = request.body.validate[Vocabulary]
	  placeResult.fold(
	    errors => {
	      BadRequest(Json.obj("status" ->"KO", "message" -> JsError.toFlatJson(errors)))
	    },
	    movie => { 
	      daoVocabulary.save(movie)
	      Ok(Json.obj("status" ->"OK", "message" -> ("Br '"+movie.foreignWord+"' saved.") ))  
	    }
	  )
  }
}