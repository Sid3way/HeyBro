package example
import com.softwaremill.sttp._
import play.api.libs.json._
import strava.stravaApi

import heybro.flickr.FlickrApi

object Hello extends Greeting with App {
  stravaApi.retreiveNewActivities()
}

trait Greeting {
  lazy val greeting: String = "hello"

  // TODO: Move in config file
  lazy val flickrAlbumId = ""
  lazy val flickrApiKey = ""

  lazy val flickrApi = new FlickrApi(flickrApiKey)

  flickrApi.getPhotos(flickrAlbumId).fold(
    error => {
      println(s"Unable to get photos from album: $error")
    },
    photoset => photoset.photo.foreach { photo =>
      flickrApi.getLocation(photo.id).fold(
        error => {
          println(s"Unable to get photos from album: $error")
        },
        photoLoc =>
          println(s"Photo no${photo.id} ${photo.title}: ${photoLoc.location}")
      )
    }
  )

}
