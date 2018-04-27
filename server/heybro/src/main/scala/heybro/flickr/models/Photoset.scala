package heybro.flickr.models

import play.api.libs.json.{Format, Json}

case class Photoset(
                   id: String,
                   ownername: String,
                   photo: Seq[PhotosetItem]
                   )

object Photoset {
  implicit val jsonFormat: Format[Photoset] = Json.format[Photoset]
}