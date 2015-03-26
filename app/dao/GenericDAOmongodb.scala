package dao

import java.util.concurrent.Executor

import scala.Option.option2Iterable
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import play.api.Play.current
import play.api.libs.json.JsObject
import play.api.libs.json.JsValue
import play.api.libs.json.Json
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json.__
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.BSONFormats.BSONDocumentFormat
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.Producer.nameValue2Producer

trait datainformation {
  def name: String;
  def getId: Option[String];
}

/*
 * attempt to create some a generic dao for mongodb for all object
 * who implements the methods defined by datainformation
 * and some transformation ( reader/writer JsValue)
 */
class genericdaomongodb[T <: datainformation](collectionName:String) {

  /*
   * reactive mongodb configuration
   */
  implicit val synchronousExecutionContext = ExecutionContext.fromExecutor(new Executor {
    def execute(task: Runnable) = task.run()
  })

  lazy val db = ReactiveMongoPlugin.db

  def collection: JSONCollection = db.collection[JSONCollection](collectionName)
  /**/

  def getAll()(implicit reader: JsValue => Option[T]): Future[List[T]] = {
    val cursor: Cursor[JsObject] = collection.find(Json.obj()).cursor[JsObject]
    // gather all the JsObjects in a list
    val futureObjectsList: Future[List[JsObject]] = cursor.collect[List]()
    futureObjectsList.map { ourlistobject => ourlistobject.map(reader(_)).flatten
    }
  }

  def save(objecttosave: T)(implicit writer: T => JsValue, changeid: (T, Option[String]) => T): Future[T] = {
    val newId = BSONObjectID.generate
    val newinstance = changeid(objecttosave, Some(newId.stringify));
    val ourjson = writer(newinstance)

    val transformAddId = (__).json.update(
      __.read[JsObject].map { o => o ++ Json.obj(("_id") -> newId) })

    val ourjsonwithid = ourjson.transform(transformAddId).get
    collection.insert(ourjsonwithid).map {
      result =>
        newinstance
    }
  }

  def get(id: String)(implicit reader: JsValue => Option[T]): Future[Option[T]] = {
    val oneobject = collection.find(BSONDocument("_id" -> BSONObjectID(id))).one[JsObject];
    val futurresult = oneobject.map { result =>

      val response = for (
        ourjsobject <- result;
        ourinstance <- reader(ourjsobject)
      ) yield { ourinstance }

      response
    }
    futurresult
  }
  
  def delete(id:String):Future[Unit]={
    collection.remove(BSONDocument("_id" -> BSONObjectID(id))).map{_=>()}
  }
  
  def update(instance: T)(implicit writer: T => JsValue):Future[T]={    
    instance.getId match{
      case Some(id) =>
        val instanceasjsion=writer(instance)       
        collection.update(BSONDocument("_id" -> BSONObjectID(id)),instanceasjsion).map{
          result=>  instance 
        }
      case _ => Future.failed(new Exception("id not defined"))
    }
		 
  }
}

