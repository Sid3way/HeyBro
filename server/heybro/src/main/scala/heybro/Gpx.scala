package heybro

import java.io.InputStream

import scala.util.{Failure, Success, Try}
import scala.xml.transform.{RewriteRule, RuleTransformer}
import scala.xml._

object Gpx {

  private object RemoveExtension extends RewriteRule {

    override def transform(n: Node): Seq[Node] =n match {
      case elem if elem.label == "extensions" => Nil
      case _ => n
    }

  }

  private object RemoveNamespace extends RewriteRule {
    override def transform(n: Node): Seq[Node] = n match {
      case e: Elem => e.copy(scope = TopScope)
      case n => n
    }
  }

  private def load(is: InputStream) = Try(XML.load(is))
  
  private def readAll(inputStreams: Seq[InputStream]) = {
    inputStreams.view.map(load)
      .foldLeft[Either[Seq[Throwable], Seq[Elem]]](Right(Nil)) {
      case (Right(xmls), Success(xml)) => Right(xmls :+ xml)
      case (Right(xmls), Failure(e)) => Left(Seq(e))
      case (Left(es), Success(_)) => Left(es)
      case (Left(es), Failure(e)) => Left(es :+ e)
    }
  }

  /** Merge and sanitize GPX files (no validation) */
  def process(inputStreams: InputStream*): Either[Seq[Throwable], Elem] = {
    for (xmls <- readAll(inputStreams)) yield {
      val sanitize = new RuleTransformer(RemoveNamespace, RemoveExtension)

      <gpx version="1.1"
                        xsi:schemaLocation="http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/11.xsd"
                        xmlns="http://www.topografix.com/GPX/1/1"
                        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
        { xmls.flatMap(sanitize(_) \ "trk") }
      </gpx>
    }
  }

}
