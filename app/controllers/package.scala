import play.api.libs.json._

package object controllers {

  /**
    * Json writer for popular queries
    */
  implicit val popularWrites = new Writes[List[(String, Int)]] {
    def writes(popularQueries: List[(String, Int)]) =
      Json.arr(popularQueries.map {
        case (url, count) => JsObject(Seq("query" -> JsString(url), "count" -> JsNumber(count)))
      })
  }
}
