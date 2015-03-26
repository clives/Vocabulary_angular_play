package dao

import models.Vocabulary
import scala.concurrent.Future
import scala.util.Try
import play.api.libs.json.JsValue

trait DAOVocabulary {
  
  def getAll()(implicit reader: JsValue => Option[Vocabulary]):Future[List[Vocabulary]];
  
  def save(vocabulary: Vocabulary)(implicit writer: Vocabulary => JsValue, changeid: (Vocabulary, Option[String]) => Vocabulary):Future[Vocabulary];
  
  def get(id:String)(implicit reader: JsValue => Option[Vocabulary]):Future[Option[Vocabulary]];
  
  def delete(id:String):Future[Unit];
  
  def update(vocabulary: Vocabulary)(implicit writer: Vocabulary => JsValue):Future[Vocabulary];
}