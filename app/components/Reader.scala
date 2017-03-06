package components

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import models.Query

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Read csv file
  */
// TODO read gz file
object Reader {

  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  private val tsvSeparator = "\t"

  def stream(path:String): Stream[Query] = streamCache.get(path)

  private lazy val streamCache: LoadingCache[String, Stream[Query]] = CacheBuilder.newBuilder()
    .expireAfterWrite(30, TimeUnit.MINUTES)
    .build[String, Stream[Query]](new CacheLoader[String, Stream[Query]] {
    override def load(key: String): Stream[Query] = {
      println("read")
      read(key,tsvSeparator).fold(err => Stream.empty,identity)
    }
  })
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
