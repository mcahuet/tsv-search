package services

import java.time.LocalDateTime

import components.CsvReader
import models.{Query, Year}
import utils.BaseSpec


class SearchSpec extends BaseSpec {

  private val reader = mock[CsvReader]
  private val searchReturnStream = new Search(reader, app.configuration) {
    override val maybeQueries = Right(Stream(Query(LocalDateTime.of(2015, 2, 1, 0, 0, 0), "query1"), Query(LocalDateTime.of(2015, 3, 1, 0, 0, 0), "query2")))
  }
  private val searchReturnNothing = new Search(reader, app.configuration) {
    override val maybeQueries = Left("nothing")
  }
  private val temporal = Year(LocalDateTime.of(2015, 1, 1, 0, 0, 0))
  private val limit = 10


  "Search.count" must {
    "return Some" in {
      val count = searchReturnStream.count(temporal)

      count must be(Some(2))
    }

    "return None" in {
      val count = searchReturnNothing.count(temporal)

      count must be(None)
    }
  }
  "Search.popular" must {

    "return Some" in {
      val popular = searchReturnStream.popular(temporal, limit)

      popular must be(List(("query1", 1), ("query2", 1)))
    }

    "return None" in {
      val popular = searchReturnNothing.popular(temporal, limit)

      popular must be(Nil)
    }
  }
}
