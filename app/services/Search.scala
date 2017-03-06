package services

import javax.inject.Inject

import components.Reader
import models.{Query, QueryDateTime}
import play.api.Configuration

/**
  * Search service to search and filter queries
  *
  * @param configuration is application configuration
  */
class Search @Inject()(configuration: Configuration) {

  private val queryFilePath = configuration.underlying.getString("query.file.path")
  val maybeQueries: Stream[Query] = Reader.stream(queryFilePath)

  /**
    * Count of distinct queries over a period
    *
    * @param temporal is the period request
    * @return filtered queries by period
    */
  def count(temporal: QueryDateTime): Option[Int] = {
    if (maybeQueries.isEmpty) {
      None
    } else {
      Some(maybeQueries.foldLeft(Set.empty[String]) { (result: Set[String], query: Query) =>
        if (temporal.filter(query.date)) {
          result + query.url
        } else {
          result
        }
      }.size)
    }
  }

  /**
    * Popular queries over a period
    *
    * @param temporal is the period request
    * @param limit    is the number of expected result
    * @return filtered queries by period
    */
  def popular(temporal: QueryDateTime, limit: Int): List[(String, Int)] = {
    if (maybeQueries.isEmpty) {
      Nil
    } else {
      maybeQueries.foldLeft(Map.empty[String, Int]) { (result, query) =>
        if (temporal.filter(query.date)) {
          result + result.get(query.url).map(acc => (query.url, acc + 1)).getOrElse((query.url, 1))
        } else {
          result
        }
      }.toList.sortWith(_._2 > _._2).take(limit)
    }
  }
}
