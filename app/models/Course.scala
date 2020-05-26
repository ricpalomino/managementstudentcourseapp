package models

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._

case class Course(_id: Option[BSONObjectID],
                  name: String)

object Course {
  implicit val format = Json.format[Course]
}
