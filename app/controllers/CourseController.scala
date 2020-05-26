package controllers

import io.swagger.annotations._
import javax.inject._
import models.Course
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import services.CourseService
import utils.ResponseMessageUtil._

import scala.concurrent.{ExecutionContext, Future}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
@Api(value = "/courses")
class CourseController @Inject()(val cc: ControllerComponents,
                                 implicit val ex: ExecutionContext,
                                 courseService: CourseService
                                 ) extends AbstractController(cc) {

  @ApiOperation(
    value = "Find all Courses",
    response = classOf[Course],
    responseContainer = "List"
  )
  def list() = Action.async {
    courseService.list(100).map { courses =>
      Ok(Json.toJson(courses))
    }
  }

  @ApiOperation(
    value = "Find a Course by Id",
    response = classOf[Course]
  )
  @ApiResponses(Array(
    new ApiResponse(code = 404, message = "Course not found")
  )
  )
  def get(id: BSONObjectID) = Action.async {
    courseService.get(id).map { res =>
      Status(res.code)(Json.obj(STATUS -> res.code,
        MESSAGE -> res.message,
        DATA -> Json.toJson(res.model)))
    }
  }

  @ApiOperation(
    value = "Create a new Course to the list",
    response = classOf[Void],
    code = 201
  )
  @ApiResponses(Array(
    new ApiResponse(code = 400, message = "Invalid Course format")
  )
  )
  @ApiImplicitParams(Array(
    new ApiImplicitParam(value = "The Course to add, in Json Format", required = true, dataType = "models.Course", paramType = "body")
  )
  )
  def create() = Action.async(parse.json) {
    _.body.validate[Course].map { course =>
      courseService.save(course).map { res =>
        Status(res.code)(Json.obj(STATUS -> res.code, MESSAGE -> res.message))
      }
    }
      .getOrElse(Future.successful(
        BadRequest(Json.obj(STATUS -> STATUS_ERROR, MESSAGE -> ERROR_REQUEST_DATA))
      ))
  }

}

