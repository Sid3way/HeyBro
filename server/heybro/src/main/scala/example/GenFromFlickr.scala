package example

import heybro.GeoLoc
import heybro.flickr.FlickrApi
import heybro.kml.KmlGen
import heybro.kml.KmlGen.ImageInfo

object GenFromFlickr extends App {
  lazy val flickrApiKey = ""
  lazy val flickrAlbumId = ""
  val api = new FlickrApi(flickrApiKey)

  api.getPhotos(flickrAlbumId) match {
    case Right(photoset) =>
      val photos = photoset.photo.map(photo => {

        val locatioOpt = api.getLocation(photo.id).toOption

        val urlEither = api.getPhotoUrl(photo.id)

        locatioOpt.map { loc =>
          ImageInfo(
            urlEither.toOption.get,
            GeoLoc(loc.location.latitude.toDouble, loc.location.longitude.toDouble)
          )
        }
      })
      println(KmlGen.imagesToKml(photos.flatten))
  }

}
