package services

import java.util.concurrent.Future

import javax.inject.Inject
import models.Course
import play.api.Logger
import play.api.http.Status._
import play.api.libs.json.Json
import reactivemongo.bson.{BSONObjectID, BSONString}
import reactivemongo.core.errors.DatabaseException
import repositories.CourseRepository
import utils.ResponseMessage
import utils.ResponseMessageUtil._

import scala.concurrent.ExecutionContext

class CourseService @Inject()( implicit val ex: ExecutionContext,
                               courseRepository: CourseRepository)
{

  val logger = Logger(this.getClass)

  def save(course: Course) = {
    courseRepository.create(course).map {
      case insert if insert.ok =>
        logger.info(s" $DATA_INSERT_SUCCESS $course")
        ResponseMessage(CREATED, DATA_INSERT_SUCCESS)
      case insert if insert.writeConcernError.isDefined =>
        logger.error(s" $DATA_INSERT_FAILURE ${insert.writeConcernError.get.errmsg}")
        ResponseMessage(insert.writeConcernError.get.code,insert.writeConcernError.get.errmsg)
    }recover {
      case dex: DatabaseException => logger.error("Database exception",dex)
        ResponseMessage(dex.code.getOrElse(BAD_REQUEST),dex.getMessage())
      case e: Throwable => logger.error("Error save",e)
        ResponseMessage(INTERNAL_SERVER_ERROR,e.getMessage)
    }
  }

  def list(limit: Int) = {
    courseRepository.list(limit)
  }

  def get(id: BSONObjectID) = {
    courseRepository.get(id).map {
      case Some(course) =>
        logger.info(s" $DATA_GET_SUCCESS $course")
        ResponseMessage(message = DATA_GET_SUCCESS, model = Some(Json.toJson(course)))
      case None =>
        logger.warn(s"Course doesn't exist with id $id")
        ResponseMessage(NOT_FOUND,DATA_NOT_EXIST)
    }recover {
      case dex: DatabaseException => logger.error("Database exception",dex)
        ResponseMessage(dex.code.getOrElse(BAD_REQUEST),dex.getMessage())
      case e: Throwable => logger.error("Error save",e)
        ResponseMessage(INTERNAL_SERVER_ERROR,e.getMessage)
    }
  }

  def existCourseIds(courseIds: List[String]) = courseRepository.findByCourseIds(courseIds)


}
