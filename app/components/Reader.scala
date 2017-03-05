package components

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import models.Query

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Read csv file
  */
// TODO read gz file
class CsvReader {

  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  /**
    * Read file
    *
    * @param path      is the path of file
    * @param separator is the separator of columns
    * @return a stream of file
    */
  def read(path: String, separator: String): Either[String, Stream[Query]] = {
    Try(Source.fromFile(path)) match {
      case Success(file) => {
        Right(file.getLines().toStream.map { line =>
          val columns: Array[String] = line.split(separator).map(_.trim)
          Query(LocalDateTime.parse(columns(0), dateTimeFormatter), columns(1))
        })
      }
      case Failure(err) => Left(err.getMessage)
    }
  }
}
