package heybro.flickr.models

import play.api.libs.json.Json

case class PhotoSize(label: String, url: String)

object PhotoSize {

  implicit val format = Json.format[PhotoSize]

}
