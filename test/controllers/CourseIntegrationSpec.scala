package controllers

import models.Course
import org.scalatest.BeforeAndAfter
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CourseIntegrationSpec extends PlayWithMongoSpec with BeforeAndAfter {

  var Courses: Future[JSONCollection] = _

  before {
    //Init DB
    await {
      Courses = reactiveMongoApi.database.map(_.collection("courses"))

      Courses.flatMap(_.insert[Course](ordered = false).many(List(
        Course(_id = None, name = "Course-1"),
        Course(_id = None, name = "Course-2")
      )))
    }
  }

  after {
    //clean DB
    Courses.flatMap(_.drop(failIfNotFound = false))
  }

  "Get all Courses" in {
    val Some(result) = route(app, FakeRequest(GET, "/courses"))
    val resultList = contentAsJson(result).as[List[Course]]
    resultList.length mustEqual 2
    status(result) mustBe OK
  }

  "Add a Course" in {
    val payload = Course(_id = None, name = "Course-3")
    val Some(result) = route(app, FakeRequest(POST, "/courses").withJsonBody(Json.toJson(payload)))
    status(result) mustBe CREATED
  }


}
