package models

import java.time.LocalDateTime

/**
  * The query model extracted from the file
  * @param date
  * @param url
  */
case class Query(date: LocalDateTime, url: String)