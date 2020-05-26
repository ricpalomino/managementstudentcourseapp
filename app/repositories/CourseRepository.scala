package repositories

import javax.inject.Inject
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.{BSONArray, BSONDocument, BSONDocumentReader, BSONObjectID, BSONString}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.api._
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.play.json._
import models.Course
import reactivemongo.api.collections.bson.BSONCollection

class CourseRepository @Inject()(implicit ex: ExecutionContext,
                                 reactiveMongoApi: ReactiveMongoApi)
{

  private def collection:Future[JSONCollection] =
    reactiveMongoApi.database.map(_.collection("courses"))

  def list(limit: Int = 100): Future[Seq[Course]] =
    collection.flatMap(
      _.find(BSONDocument())
        .cursor[Course](ReadPreference.primary)
        .collect[Seq](limit, Cursor.FailOnError[Seq[Course]]())
    )

  def create(course: Course): Future[WriteResult] =
    collection.flatMap(_.insert(course))

  def get(id: BSONObjectID): Future[Option[Course]] =
    collection.flatMap(_.find(BSONDocument("_id" -> id)).one[Course])

  def findByCourseIds(ids: Seq[String]) =
    collection.flatMap(_.find(
      BSONDocument("_id" -> BSONDocument("$in" -> BSONArray(ids.map(BSONObjectID(_)))))).cursor[Course](ReadPreference.primary)
      .collect[Seq](-1, Cursor.FailOnError[Seq[Course]]())
    )

}
