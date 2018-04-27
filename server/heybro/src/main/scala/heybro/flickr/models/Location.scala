package heybro.flickr.models

import play.api.libs.json.{Format, Json}

case class Location(
  latitude: String,
  longitude: String,
  accuracy: String
)

object Location {
  implicit val jsonFormat: Format[Location] = Json.format[Location]
}