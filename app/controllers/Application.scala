package controllers

import javax.inject.Inject

import components.QueryTemporal
import play.api.libs.json._
import play.api.mvc._
import services.Search

/**
  * Controller of popular and count
  *
  */
class Application @Inject()() extends Controller {

  /**
    * Application status
    *
    * @return the status of application
    */
  def index = Action {
    Ok("Application is ready")
  }
}