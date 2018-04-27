package heybro.flickr.models

import play.api.libs.json.{Format, Json}

case class PhotosetItem(
  id: String,
  title: String
)

object PhotosetItem {
  implicit val jsonFormat: Format[PhotosetItem] = Json.format[PhotosetItem]
}