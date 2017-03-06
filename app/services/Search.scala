package services

import javax.inject.Inject

import components.Reader
import models.{Query, QueryDateTime}
import play.api.Configuration

class Search @Inject()(configuration: Configuration) {

  private val queryFilePath = configuration.underlying.getString("query.file.path")
  val maybeQueries: Option[Stream[Query]] = Option(Reader.stream(queryFilePath))

  def count(temporal: QueryDateTime): Option[Int] = {
    // reduce
    maybeQueries.map(_.count(query => temporal.filter(query.date)))
    maybeQueries.map{ queries =>
      queries.foldLeft(Set.empty[String]) { (result: Set[String], query: Query) =>
        if (temporal.filter(query.date)) {
          result + query.url
        } else {
          result
        }
      }.size
    }
  }

  def popular(temporal: QueryDateTime, limit: Int): List[(String, Int)] = {
    maybeQueries match {
      case None => Nil
      case Some(queries) => queries.foldLeft(Map.empty[String, Int]) { (result, query) =>
        if (temporal.filter(query.date)) {
          result + result.get(query.url).map(acc => (query.url, acc + 1)).getOrElse((query.url, 1))
        } else {
          result
        }
      }.toList.sortWith(_._2 > _._2).take(limit)
    }
  }
}
