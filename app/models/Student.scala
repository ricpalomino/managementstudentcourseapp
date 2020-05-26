package models

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import reactivemongo.play.json._

case class Student(_id: Option[BSONObjectID],
                   firstName: String,
                   lastName: String,
                   courseIds: List[String])

object Student {
  implicit val format = Json.format[Student]
}

