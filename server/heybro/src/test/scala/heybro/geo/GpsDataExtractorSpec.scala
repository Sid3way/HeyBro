package heybro.geo

import heybro.GeoLoc
import heybro.exif.GpsDataExtractor
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Success

class GpsDataExtractorSpec extends FlatSpec with Matchers {

  "getGeoLocation with a non tagged photo" should "return Success(None)" in {

    val notGeoTagged = getClass.getResourceAsStream("/geoimages/notgeotaggedimage.jpg")

    GpsDataExtractor.getGeoLocation(notGeoTagged) shouldBe Success(None)
  }

  "getGeoLocation with a geo tagged photo" should "return Success(Some(...))" in {

    val notGeoTagged = getClass.getResourceAsStream("/geoimages/ibiza.jpg")

    GpsDataExtractor.getGeoLocation(notGeoTagged) shouldBe Success(Some(GeoLoc(38.90983333333333, 1.4386666666666668)))
  }
}
