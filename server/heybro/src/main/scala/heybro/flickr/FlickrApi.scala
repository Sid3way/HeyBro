package heybro.flickr

import heybro.flickr.models.{PhotoLocation, Photoset}
import play.api.libs.json.Json

class FlickrApi(apiKey: String) {

  import com.softwaremill.sttp._
  implicit val backend = HttpURLConnectionBackend()

  def getPhotos(albumId: String): Either[String, Photoset] = {
    val response = sttp.get(uri"https://api.flickr.com/services/rest/?method=flickr.photosets.getPhotos&api_key=$apiKey&photoset_id=$albumId&format=json&nojsoncallback=1")
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

}
