package daotest

import scala.concurrent.{Await,Future}
import scala.concurrent.duration.{DurationInt,FiniteDuration}
import scala.util.{Failure,Success,Try}
import org.scalatestplus.play.OneServerPerSuite
import org.scalatestplus.play.PlaySpec
import dao.DAOVocabularyMongodb
import models.Vocabulary
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.test.FakeApplication
import dao.genericdaomongodb


/*
 * base sur https://www.playframework.com/documentation/2.2.x/ScalaFunctionalTestingWithScalaTest
 */
class TestDAOGenericMongo extends PlaySpec with OneServerPerSuite {

   // Override app if you need a FakeApplication with other than
  // default parameters.
  implicit override lazy val app: FakeApplication =
    FakeApplication(
      additionalPlugins = List("play.modules.reactivemongo.ReactiveMongoPlugin")
    )
   
		val timeout: FiniteDuration = DurationInt(10).seconds

		
		"test save, update, delete" in {
	     
		val myPublicAddress =  s"localhost:$port"

		println(s"START  $myPublicAddress")

		import Vocabulary._
		
		val ourdao=new genericdaomongodb[Vocabulary]("Vocabulary");
    val voc1=new Vocabulary( "brword", "frword", "example...", None,None);
    println("START")

    //change try to future to return a futur with a try => regroup the erros using
    //future.failure
    val result = for (
      ourvocabulary <- ourdao.save(voc1);
      vocabularyupdated <- ourdao.update(ourvocabulary.copy(foreignWord = "_updat_ed_"));
      // testfail <- Future.failed(new Exception);
      _ <-ourdao.delete(vocabularyupdated.id.get)   
    ) yield {
      vocabularyupdated
    }

    try {
      val ourrespone = Await.result(result, timeout)
      ourrespone.foreignWord mustEqual "_updat_ed_"
    } catch {
      case e: Exception =>
        e.printStackTrace()
        fail()
    }         
	}
}