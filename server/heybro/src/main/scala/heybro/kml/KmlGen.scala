package heybro.kml

import heybro.GeoLoc

object KmlGen {

  case class ImageInfo(href: String, geoLoc: GeoLoc, comment: Option[String] = None)

  def imagesToKml(images: Seq[ImageInfo]) = {
    <kml xmlns="http://www.opengis.net/kml/2.2">
      <Document>
        {images.map(imgToPlacemark)}
      </Document>
    </kml>

  }

  private def imgToPlacemark(imgInfo: ImageInfo) = {
    val ImageInfo(href, geoLoc, comment) = imgInfo
    <Placemark>
      <description>
        { comment.getOrElse("") }
      </description>
      <Point>
        <coordinates>{geoLoc.lat},{geoLoc.long}</coordinates>
      </Point>
      <ExtendedData>
        <Data name="gx_media_links">
          <value>{ href }</value>
        </Data>
      </ExtendedData>
    </Placemark>
  }
}
