package controllers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import models.Student
import org.scalatest.BeforeAndAfter
import play.api.libs.json.{ Json, JsObject }
import play.api.test.FakeRequest
import play.api.test.Helpers._
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._


class StudentIntegrationSpec extends PlayWithMongoSpec with BeforeAndAfter {

  var students: Future[JSONCollection] = _

  before {
    //Init DB
    await {
      students = reactiveMongoApi.database.map(_.collection("students"))

      students.flatMap(_.insert[Student](ordered = false).many(List(
        Student(_id = None, firstName = "Richard1", lastName = "Palomino1", courseIds = List()),
        Student(_id = None, firstName = "Richard2", lastName = "Palomino2", courseIds = List())
      )))
    }
  }

  after {
    //clean DB
    students.flatMap(_.drop(failIfNotFound = false))
  }

  "Get all Students" in {
    val Some(result) = route(app, FakeRequest(GET, "/students"))
    val resultList = contentAsJson(result).as[List[Student]]
    resultList.length mustEqual 2
    status(result) mustBe OK
  }

  "Add a Student" in {
    val payload = Student(_id = None, firstName = "Richard3", lastName = "Palomino3", courseIds = List())
    val Some(result) = route(app, FakeRequest(POST, "/students").withJsonBody(Json.toJson(payload)))
    status(result) mustBe CREATED
  }


}
