package heybro.exif

import java.io.{File, FileInputStream, InputStream}

import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.GpsDirectory
import heybro.GeoLoc

import scala.util.Try

object GpsDataExtractor {

  def getGeoLocation(file: InputStream) = {
    val metadataTry = Try(ImageMetadataReader.readMetadata(file))


    for(metadata <- metadataTry) yield {
      Option(metadata.getFirstDirectoryOfType(classOf[GpsDirectory]))
        .flatMap(dir => Option(dir.getGeoLocation))
        .map(geo => GeoLoc(geo.getLatitude, geo.getLongitude))
    }
  }

  def getGeoLocation(file: File) : Try[Option[GeoLoc]] = {
    Try(new FileInputStream(file)).flatMap(getGeoLocation)
  }

}
