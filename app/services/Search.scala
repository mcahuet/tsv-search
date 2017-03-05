package services

import javax.inject.Inject

import components.{CsvReader, QueryDateTime}
import models.Query
import play.api.Configuration

class Search @Inject()(reader: CsvReader, configuration: Configuration) {

  private val queryFilePath = configuration.underlying.getString("query.file.path")
  private val tsvSeparator = "\t"
  val maybeQueries: Either[String, Stream[Query]] = reader.read(queryFilePath, tsvSeparator)

  def count(temporal: QueryDateTime): Option[Int] = {
    maybeQueries.fold(err => None,
      queries => Some(queries.count(query => temporal.filter(query.date))))
  }

  def popular(temporal: QueryDateTime, limit: Int): List[(String, Int)] = {
    maybeQueries.fold(err => Nil,
      queries => {
        queries.foldLeft(Map.empty[String, Int]) { (result, query) =>
          if (temporal.filter(query.date)) {
            result + result.get(query.url).map(acc => (query.url, acc + 1)).getOrElse((query.url, 1))
          } else {
            result
          }
        }.toList.sortWith(_._2 > _._2).take(limit)
      })
  }
}
