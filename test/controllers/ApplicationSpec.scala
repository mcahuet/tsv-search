package controllers

import java.time.LocalDateTime

import components.Year
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.Search
import utils.BaseSpec
import org.mockito.Mockito._

import scala.concurrent.Future

class ApplicationSpec extends BaseSpec with Results {

  private val searchService = mock[Search]
  private val limit = 5
  private val yearBadFormat = 23
  private val year = 2015


  "Index" must {
    "return ok" in {
      val controller = new Application(searchService)

      val result: Future[Result] = controller.index().apply(FakeRequest())

      status(result) must be(OK)
    }
  }
  "Count" must {

    "not valid date format" in {
      val controller = new Application(searchService)

      val result: Future[Result] = controller.count(yearBadFormat.toString).apply(FakeRequest())

      status(result) must be(BAD_REQUEST)
    }

    "valid date format but no queries" in {

      when(searchService.count(Year(LocalDateTime.of(year,1,1,0,0,0)))) thenReturn None
      val controller = new Application(searchService)

      val result: Future[Result] = controller.count(year.toString).apply(FakeRequest())

      status(result) must be(NOT_FOUND)
    }

    "valid date format and some queries" in {

      when(searchService.count(Year(LocalDateTime.of(year,1,1,0,0,0)))) thenReturn Some(5)
      val controller = new Application(searchService)

      val result: Future[Result] = controller.count(year.toString).apply(FakeRequest())

      status(result) must be(OK)
    }
  }

  "Populat" must {

    "not valid date format" in {
      val controller = new Application(searchService)

      val result: Future[Result] = controller.popular(yearBadFormat.toString,limit).apply(FakeRequest())

      status(result) must be(BAD_REQUEST)
    }

    "valid date format but no queries" in {

      when(searchService.popular(Year(LocalDateTime.of(year,1,1,0,0,0)),limit)) thenReturn Nil
      val controller = new Application(searchService)

      val result: Future[Result] = controller.popular(year.toString,limit).apply(FakeRequest())

      status(result) must be(NOT_FOUND)
    }

    "valid date format and some queries" in {

      when(searchService.popular(Year(LocalDateTime.of(year,1,1,0,0,0)),limit)) thenReturn List(("query1",1),("query2",3))
      val controller = new Application(searchService)

      val result: Future[Result] = controller.popular(year.toString,limit).apply(FakeRequest())

      status(result) must be(OK)
    }
  }
}
