import dao._
import utils.InfoMongodbConnection
import models.Vocabulary
package object VocabularyApplication {
	import play.api.Play._
	import play.api.Logger

	
	val daoVocabulary: DAOVocabulary = InfoMongodbConnection.connected match{
	  case true => 
	    Logger.info("DAO: use Mongodb");
	    new genericdaomongodb[Vocabulary]("Vocabulary") with DAOVocabulary;
	  case _ => 
	    Logger.info("DAO: use mock");
	    new DAOVocabularyMock;
	}
	
}