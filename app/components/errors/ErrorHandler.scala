package components.errors

import play.api.Logger
import play.api.http.HttpErrorHandler
import play.api.http.Status._
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.Future

class ErrorHandler extends HttpErrorHandler{

  implicit val errorWriter = Json.writes[Error]

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = Future.successful {
    Logger.warn(s"Erreur sur : ${request.uri} with message : $message  and status : $statusCode")
    statusCode match {
      case NOT_FOUND => NotFound(Json.toJson(Error(s"No endpoint correpond to ${request.path} with parameters '${request.rawQueryString}'")))
      case _ => new Status(statusCode)(Json.toJson(Error(message)))
    }
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = Future.successful{
    Logger.warn(s"Erreur sur ${request.uri} with message : ${exception.getMessage}")
      InternalServerError(Json.toJson(Error(exception.getMessage)))
    }
}

case class Error(msg:String)
