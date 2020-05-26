package repositories

import javax.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json._
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.api.{Cursor, ReadPreference}
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.commands.WriteResult
import models.Student

class StudentRepository @Inject()(implicit ec:ExecutionContext,
                                  reactiveMongoApi: ReactiveMongoApi)
{

  private def collection:Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection("students"))

  def list(limit: Int = 100): Future[Seq[Student]] =
    collection.flatMap(
      _.find(BSONDocument())
        .cursor[Student](ReadPreference.primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Student]]())
    )

  def create(student: Student): Future[WriteResult] =
    collection.flatMap(_.insert(student))

  def get(id: BSONObjectID): Future[Option[Student]] =
    collection.flatMap(_.find(BSONDocument("_id" -> id)).one[Student])


}

