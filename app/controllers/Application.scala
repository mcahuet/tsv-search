package controllers

import javax.inject.Inject

import components.QueryTemporal
import play.api.libs.json._
import play.api.mvc._
import services.Search

/**
  * Controller of popular and count
  *
  * @param searchService : searche service
  */
class Application @Inject()(searchService: Search) extends Controller {

  // TODO put I18N
  private val badDateFormat = "The param date should be 'yyyy','yyyy-MM','yyyy-MM-dd','yyyy-MM-dd HH','yyyy-MM-dd HH:mm'"
  private val missingQuery = "Missing query"

  /**
    * Application status
    *
    * @return the status of application
    */
  def index = Action {
    Ok("Application is ready")
  }

  /**
    * Count of queries
    *
    * @param maybeDate is date for filter
    * @return count of queries in json format
    */
  def count(maybeDate: String) = Action {
    maybeDate match {
      case QueryTemporal(temporal) => {
        searchService.count(temporal) match {
          case None => NotFound(missingQuery)
          case Some(count) => Ok(Json.obj("count" -> count))
        }
      }
      case _ => BadRequest(badDateFormat)
    }
  }

  /**
    *
    * @param maybeDate is date for filter
    * @param limit     is the number of expected results
    * @return the most popular queries
    */
  def popular(maybeDate: String, limit: Int) = Action {
    maybeDate match {
      case QueryTemporal(temporal) => {
        val popularQuery = searchService.popular(temporal, limit)
        if (popularQuery.isEmpty) {
          InternalServerError(missingQuery)
        } else {
          Ok(Json.obj("queries" -> Json.toJson(popularQuery)))
        }
      }
      case _ => BadRequest(badDateFormat)
    }
  }
}