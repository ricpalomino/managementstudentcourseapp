package services

import javax.inject.Inject
import models.Student
import play.api.Logger
import reactivemongo.core.errors.DatabaseException
import repositories.StudentRepository
import utils.ResponseMessage
import utils.ResponseMessageUtil._
import play.api.http.Status._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.concurrent.ExecutionContext

class StudentService @Inject()( implicit val ex: ExecutionContext,
                                studentRepository: StudentRepository,
                                courseService: CourseService)
{

  val logger = Logger(this.getClass)

  def save(student: Student) = {
    studentRepository.create(student).map {
      case insert if insert.ok =>
        logger.info(s" $DATA_INSERT_SUCCESS $student")
        ResponseMessage(CREATED,DATA_INSERT_SUCCESS)
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
    studentRepository.list(limit)
  }

  def get(id: BSONObjectID) = {
    studentRepository.get(id).map {
      case Some(student) =>
        logger.info(s" $DATA_GET_SUCCESS $student")
        ResponseMessage(message = DATA_GET_SUCCESS, model = Some(Json.toJson(student)))
      case None =>
        logger.warn(s"STUDENT doesn't exist with id $id")
        ResponseMessage(NOT_FOUND,DATA_NOT_EXIST)
    }recover {
      case dex: DatabaseException => logger.error("Database exception",dex)
        ResponseMessage(dex.code.getOrElse(BAD_REQUEST),dex.getMessage())
      case e: Throwable => logger.error("Error save",e)
        ResponseMessage(INTERNAL_SERVER_ERROR,e.getMessage)
    }
  }

}
