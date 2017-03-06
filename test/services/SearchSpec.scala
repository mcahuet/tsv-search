package services

import java.time.LocalDateTime

import components.BaseSpec
import models.{Query, Year}


class SearchSpec extends BaseSpec {

  private val searchReturnStream = new Search(app.configuration) {
    override val maybeQueries = Stream(Query(LocalDateTime.of(2015, 2, 1, 0, 0, 0), "query1"), Query(LocalDateTime.of(2015, 3, 1, 0, 0, 0), "query2"), Query(LocalDateTime.of(2015, 2, 1, 0, 0, 0), "query1"))
  }
  private val searchReturnNothing = new Search(app.configuration) {
    override val maybeQueries = Stream.empty
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

      popular must be(Some(List(("query1", 2), ("query2", 1))))
    }

    "return None" in {
      val popular = searchReturnNothing.popular(temporal, limit)

      popular must be(None)
    }
  }
}
