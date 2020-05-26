package controllers

import io.swagger.annotations._
import javax.inject._
import models.Student
import play.api._
import play.api.http.Status.NOT_FOUND
import play.api.mvc._
import play.api.libs.json._
import reactivemongo.bson.BSONObjectID
import utils.ResponseMessageUtil._

import scala.concurrent.{ExecutionContext, Future}
import services.{CourseService, StudentService}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
@Api(value = "/students")
class StudentController @Inject()(val cc: ControllerComponents,
                                  implicit val ex: ExecutionContext,
                                  studentService: StudentService,
                                  courseService: CourseService,
                                 ) extends AbstractController(cc) {

  val logger = Logger(this.getClass)

  @ApiOperation(
    value = "Find all Students",
    response = classOf[Student],
    responseContainer = "List"
  )
  def list() = Action.async {
    studentService.list(100).map { students =>
      Ok(Json.toJson(students))
    }
  }

  @ApiOperation(
    value = "Find a Student by Id",
    response = classOf[Student]
  )
  @ApiResponses(Array(
      new ApiResponse(code = 404, message = "Student not found")
    )
  )
  def get(id: BSONObjectID) = Action.async {
    studentService.get(id).map { res =>
      Status(res.code)(Json.obj(STATUS -> res.code,
        MESSAGE -> res.message,
        DATA -> Json.toJson(res.model)))
    }
  }

  @ApiOperation(
    value = "Create a new Student to the list",
    response = classOf[Void],
    code = 201
  )
  @ApiResponses(Array(
      new ApiResponse(code = 400, message = "Invalid Student format")
    )
  )
  @ApiImplicitParams(Array(
      new ApiImplicitParam(value = "The Student to add, in Json Format", required = true, dataType = "models.Student", paramType = "body")
    )
  )
  def create() = Action.async(parse.json) {
    _.body.validate[Student].map { student =>
      (for {
        courses <- courseService.existCourseIds(student.courseIds)
        if student.courseIds.isEmpty || (student.courseIds.nonEmpty && courses.nonEmpty)
        res <- studentService.save(student).map { res =>
          Status(res.code)(Json.obj(STATUS -> res.code, MESSAGE -> res.message))
        }
      } yield res).recover {
        case e: NoSuchElementException =>  BadRequest(Json.obj(STATUS -> NOT_FOUND, MESSAGE -> ERROR_DATA_COURSE_IDS))
        case e: IllegalArgumentException =>  BadRequest(Json.obj(STATUS -> NOT_FOUND, MESSAGE -> ERROR_DATA_COURSE_IDS))
        case _ =>  BadRequest(Json.obj(STATUS -> INTERNAL_SERVER_ERROR, MESSAGE -> DATA_INSERT_FAILURE))
      }
    }
      .getOrElse(Future.successful(
        BadRequest(Json.obj(STATUS -> STATUS_ERROR, MESSAGE -> ERROR_REQUEST_DATA))
      ))
  }

}

