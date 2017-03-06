package controllers

import javax.inject.Inject

import models.QueryDateTime
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
    * @param temporal is date for filter
    * @return count of queries in json format
    */
  def count(temporal: QueryDateTime) = Action {
    searchService.count(temporal) match {
      case None => NotFound(missingQuery)
      case Some(count) => Ok(Json.obj("count" -> count))
    }
  }

  /**
    * Popular of queries
    * @param temporal is date for filter
    * @param limit    is the number of expected results
    * @return the most popular queries
    */
  def popular(temporal: QueryDateTime, limit: Int) = Action {
    val popularQuery = searchService.popular(temporal, limit)
    if (popularQuery.isEmpty) {
      NotFound(missingQuery)
    } else {
      Ok(Json.obj("queries" -> Json.toJson(popularQuery)))
    }
  }
}