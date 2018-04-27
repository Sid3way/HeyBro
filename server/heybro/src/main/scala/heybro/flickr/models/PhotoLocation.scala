package heybro.flickr.models

import play.api.libs.json.{Format, Json}

case class PhotoLocation(
  id: String,
  location: Location
)

object PhotoLocation {
  implicit val jsonFormat: Format[PhotoLocation] = Json.format[PhotoLocation]
}