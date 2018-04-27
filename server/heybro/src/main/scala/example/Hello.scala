package example
import com.softwaremill.sttp._
import play.api.libs.json._
import strava.stravaApi

object Hello extends Greeting with App {
  stravaApi.retreiveNewActivities()
}

trait Greeting {
  lazy val greeting: String = "hello"
}
