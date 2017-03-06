package controllers

import java.time.LocalDateTime

import components.BaseSpec
import models.Year
import org.mockito.Mockito._
import play.api.mvc.{Result, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.Search

import scala.concurrent.Future

class ApplicationSpec extends BaseSpec with Results {

  private val searchService = mock[Search]
  private val limit = 5
  private val year = Year(LocalDateTime.of(2015, 1, 1, 0, 0, 0))


  "Index" must {
    "return ok" in {
      val controller = new Application(searchService)

      val result: Future[Result] = controller.index().apply(FakeRequest())

      status(result) must be(OK)
    }
  }
  "Count" must {

    "valid date format but no queries" in {

      when(searchService.count(year)) thenReturn None
      val controller = new Application(searchService)

      val result: Future[Result] = controller.count(year).apply(FakeRequest())

      status(result) must be(NOT_FOUND)
    }

    "valid date format and some queries" in {

      when(searchService.count(year)) thenReturn Some(5)
      val controller = new Application(searchService)

      val result: Future[Result] = controller.count(year).apply(FakeRequest())

      status(result) must be(OK)
    }
  }

  "Populat" must {

    "valid date format but no queries" in {

      when(searchService.popular(year, limit)) thenReturn Nil
      val controller = new Application(searchService)

      val result: Future[Result] = controller.popular(year, limit).apply(FakeRequest())

      status(result) must be(NOT_FOUND)
    }

    "valid date format and some queries" in {

      when(searchService.popular(year, limit)) thenReturn List(("query1", 1), ("query2", 3))
      val controller = new Application(searchService)

      val result: Future[Result] = controller.popular(year, limit).apply(FakeRequest())

      status(result) must be(OK)
    }
  }
}
