package components

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import models.Query
import play.api.Logger

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Read tsv file
  */
// TODO read gz file
object Reader {

  val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  private val tsvSeparator = "\t"

  def stream(path: String): Stream[Query] = streamCache.get(path)

  private lazy val streamCache: LoadingCache[String, Stream[Query]] = CacheBuilder.newBuilder()
    .expireAfterWrite(30, TimeUnit.MINUTES)
    .build[String, Stream[Query]](new CacheLoader[String, Stream[Query]] {
    override def load(key: String): Stream[Query] = {
      read(key, tsvSeparator).getOrElse(Stream.empty)
    }
  })

  /**
    * Read file
    *
    * @param path      is the path of file
    * @param separator is the separator of columns
    * @return a stream of file
    */
  def read(path: String, separator: String): Option[Stream[Query]] = {
    Try{
      val file = Source.fromFile(path)
      file.getLines().toStream.map { line =>
        val columns: Array[String] = line.split(separator).map(_.trim)
        Query(LocalDateTime.parse(columns(0), dateTimeFormatter), columns(1))
      }
    } match {
      case Success(queries) => Some(queries)
      case Failure(err) => {
        Logger.error(s"Error during read file : $err")
        None
      }
    }
  }
}
