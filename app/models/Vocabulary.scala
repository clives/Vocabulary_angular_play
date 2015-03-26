package models


import play.api.libs.json._
import play.api.libs.functional.syntax._
import dao.datainformation


case class Vocabulary(foreignWord:String, naturalWord:String, examples: String, idsound: Option[String], id: Option[String]) extends datainformation{
    def name= "Vocabulary";
    def getId= id;
}

object Vocabulary {
  
  
  implicit val vocabularyWrites: Writes[Vocabulary] = (
	  (JsPath \ "foreignWord").write[String] and
	  (JsPath \ "naturalWord").write[String] and
	  (JsPath \ "examples").write[String] and
	  (JsPath \ "audioId").writeNullable[String] and
	  (JsPath \ "id").writeNullable[String]
  )(unlift(Vocabulary.unapply))

  implicit val vocabularyReads: Reads[Vocabulary] = (
  	(JsPath \ "foreignWord").read[String] and
	  (JsPath \ "naturalWord").read[String] and
	  (JsPath \ "examples").read[String] and
	  (JsPath \ "audioId").readNullable[String] and
	  (JsPath \ "id").readNullable[String]
  )(Vocabulary.apply _)

  implicit def read(json:JsValue):Option[Vocabulary] = vocabularyReads.reads(json).asOpt
  
  implicit def read(instance:Vocabulary):JsValue = vocabularyWrites.writes(instance)
  
   implicit def updateid(instance:Vocabulary,id:Option[String]):Vocabulary={
    instance.copy( id=id);
  }
}