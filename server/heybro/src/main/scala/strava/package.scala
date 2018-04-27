package strava

import better.files.File
import com.softwaremill.sttp._
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.json.JodaReads

object stravaApi {
  case class ActivitySummary(id: Long, start_date: DateTime)
  case class Polylines(polylines: List[String])

  implicit val dateTimeJsReader = JodaReads.jodaDateReads("yyyy-MM-dd'T'HH:mm:ssZ")

  implicit val activitySummaryReads: Reads[ActivitySummary] = (
      (JsPath \ "id").read[Long] and
      (JsPath \ "start_date").read[DateTime]
    )(ActivitySummary.apply _)

  implicit val polylinesFormat: OFormat[Polylines] = Json.format

  implicit val backend = HttpURLConnectionBackend()
  val token = "Bearer 89d8aef3fc83ef5b2d9956c03c28eb5efd6464d6"

  def retreiveNewActivities(): Unit = {
    val request = sttp
      .headers(Map("Authorization" -> token))
      // use an optional parameter in the URI
      .get(uri"https://www.strava.com/api/v3/athlete/activities")

    val response = request.send()
    val jsonString = response.body.right.get
    val json = Json.parse(response.body.right.get)

    val polylinesFiles = File("./polylines.json").createIfNotExists()
    val polylinesJson = Json.parse(polylinesFiles.contentAsString())
    var polylines = polylinesJson.asOpt[Polylines].get

    json.validate[List[ActivitySummary]] match {
      case s: JsSuccess[List[ActivitySummary]] => {
        val activities: List[ActivitySummary] = s.get
        val newPolylines: List[String] = activities.map(activity => retreiveActivityById(activity.id))
        polylines = polylines.copy(polylines = polylines.polylines ++ newPolylines)
      }
      case e: JsError => {
        println(e)
      }
    }
    polylinesFiles.overwrite(Json.stringify(Json.toJson(polylines)))
  }

  def retreiveActivityById(activityId: Long): String = {

    val request = sttp
      .headers(Map("Authorization" -> token))
      // use an optional parameter in the URI
      .get(uri"https://www.strava.com/api/v3/activities/$activityId")

    val response = request.send()
    val jsonString = response.body.right.get
    val json = Json.parse(response.body.right.get)
    val polyline = (json \ "map" \ "polyline").get
    polyline.toString()
  }
}