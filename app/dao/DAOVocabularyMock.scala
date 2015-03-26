package dao

import models.Vocabulary
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.util.Try
import scala.util.Failure
import scala.util.Success
import play.api.libs.json.JsValue

class DAOVocabularyMock extends DAOVocabulary {
  
  var list: List[Vocabulary] = {
    List(
      Vocabulary(
        "Buen dia", "Bonjour", "Buen dia, como estas? ESOSOSOSOSOSOSOSOSOJHJhjjkhjkhhjhkjhhj", None,Some("0")
      )
    )
  }
    
  
  
  var currentid=list.map(_.id).flatten.sortBy(-_.toLong).head.toLong
  
  private def nextId():Long={
    currentid+=1
    currentid
  }
  
  def getAll()(implicit reader: JsValue => Option[Vocabulary]):Future[List[Vocabulary]]={
    Future(list)
  }
  
  def save(vocabulary: Vocabulary)(implicit writer: Vocabulary => JsValue, changeid: (Vocabulary, Option[String]) => Vocabulary):Future[Vocabulary] = { 
    val newinstance=vocabulary.copy( id=Some(nextId().toString));
    list = list ::: List(newinstance)
    Future{newinstance}
  }
  
  def get(id:String)(implicit reader: JsValue => Option[Vocabulary])={
    Future{list.filter(vocabulary => vocabulary.id.exists(_==id)).headOption}
  }
  
  def delete(id:String):Future[Unit]={
    val newlist=list.filter(_.id.get!=id)

    if( newlist.size==list.size) Future{Failure(new Exception("element no present"))}
    else{
      list=newlist;
      Future{}
    }    
  }
  
  def update(vocabulary: Vocabulary)(implicit writer: Vocabulary => JsValue):Future[Vocabulary] ={
    list = vocabulary:: list.filter(_.id!=vocabulary.id);
    Future{ vocabulary}
  }
}