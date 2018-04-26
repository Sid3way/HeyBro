package heybro.gpx

import java.io.{File, FileInputStream}

import heybro.Gpx
import org.scalatest.{FlatSpec, MustMatchers}
import org.scalatest.Matchers._

import scala.xml.Elem

class GpxSpec extends FlatSpec with MustMatchers {

  val result: Either[Seq[Throwable], Elem] = Gpx.process(
    new FileInputStream(new File("./activity_1.gpx")),
    new FileInputStream(new File("./activity_2.gpx"))
  )


  "process result" should "be valid" in {
   assert(result.isRight)
  }

  "process result" should "have 2 trk children" in {
    (result.getOrElse(fail()) \ "trk").size shouldBe 2
  }
}
