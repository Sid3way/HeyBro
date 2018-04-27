package heybro.flickr

import heybro.flickr.models.{PhotoLocation, PhotoSize, Photoset}
import play.api.libs.json.Json

class FlickrApi(apiKey: String) {

  import com.softwaremill.sttp._
  implicit val backend = HttpURLConnectionBackend()

  def getPhotos(albumId: String): Either[String, Photoset] = {
    val response = sttp.get(uri"https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=$apiKey&photoset_id=$albumId&format=json&nojsoncallback=1&privacy_filter=5")
      .send()

    response.body.flatMap { data =>
      (Json.parse(data) \ "photoset").validate[Photoset].fold(
        errors => Left(s"Invalid json: $errors"),
        photoset => Right(photoset)
      )
    }
  }

  def getLocation(photoId: String): Either[String, PhotoLocation] = {
    val response = sttp.get(uri"https://api.flickr.com/services/rest/?method=flickr.photos.geo.getLocation&api_key=$apiKey&photo_id=$photoId&format=json&nojsoncallback=1")
      .send()

    response.body.flatMap { data =>
      (Json.parse(data) \ "photo").validate[PhotoLocation].fold(
        errors => Left(s"Invalid json: $errors"),
        location => Right(location)
      )
    }
  }

  def getPhotoUrl(photoId: String): Either[String, String] = {
    val response = sttp.get(uri"https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=$apiKey&photo_id=$photoId&format=json&nojsoncallback=1")
      .send()
    response.body.flatMap{ data =>
      (Json.parse(data) \ "size" \ "size").validate[Seq[PhotoSize]].fold(
        errors => Left(s"Invalid json: $errors"),
          sizes => sizes.find(_.label == "Original").map(size => Right(size.url))
            .getOrElse(Left("error: no original size found"))
        )
    }
  }

}
