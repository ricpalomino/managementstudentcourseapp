package utils


import play.api.http.Status._
import play.api.libs.json.JsValue

object ResponseMessageUtil {

  val STATUS = "status"
  val STATUS_CODE_OK = 200
  val STATUS_CODE_INTERNAL_SERVER = 500
  val STATUS_OK = "Ok"
  val STATUS_ERROR = "Error"
  val MESSAGE = "message"
  val CODE = "code"
  val DATA = "data"

  val DATA_INSERT_SUCCESS = "data saved successfully."
  val DATA_GET_SUCCESS = "data get successfully."
  val DATA_NOT_EXIST = "data not exist."
  val DATA_INSERT_FAILURE = "Error saved record."

  val ERROR_REQUEST_DATA = "There was an error with your request. Please complete required fields"
  val ERROR_DATA_COURSE_IDS = "Check courseIds values doesn't exist"
}

case class ResponseMessage(code: Int = OK , message: String, model: Option[JsValue] = None)
