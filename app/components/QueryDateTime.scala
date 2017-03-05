package components

import java.time.LocalDateTime

import play.api.mvc.PathBindable


// TODO best validation of date, return Either with error

object QueryDateTime {

  private val badDateFormat = "The param date should be 'yyyy','yyyy-MM','yyyy-MM-dd','yyyy-MM-dd HH','yyyy-MM-dd HH:mm'"

  private val yearRegex = """([0-9]{4})""".r
  private val monthRegex = """([0-9]{4})-([0-9]{2})""".r
  private val dayRegex = """([0-9]{4})-([0-9]{2})-([0-9]{2})""".r
  private val hourRegex = """([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2})""".r
  private val minuteRegex = """([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2})""".r

  def unapply(arg: String): Option[QueryDateTime] = arg match {
    case yearRegex(year) => Some(Year(LocalDateTime.of(year.toInt, 1, 1, 0, 0)))
    case monthRegex(year, month) => Some(Month(LocalDateTime.of(year.toInt, month.toInt, 1, 0, 0)))
    case dayRegex(year, month, day) => Some(Day(LocalDateTime.of(year.toInt, month.toInt, day.toInt, 0, 0)))
    case hourRegex(year, month, day, hour) => Some(Hour(LocalDateTime.of(year.toInt, month.toInt, day.toInt, hour.toInt, 0)))
    case minuteRegex(year, month, day, hour, minute) => Some(Minute(LocalDateTime.of(year.toInt, month.toInt, day.toInt, hour.toInt, minute.toInt)))
    case _ => None
  }


  implicit def binder = new PathBindable[QueryDateTime] {
    def bind(key: String, params: String): Either[String, QueryDateTime] = {
      params match {
        case QueryDateTime(p) => Right(p)
        case _ => Left(badDateFormat)
      }
    }

    def unbind(key: String, date: QueryDateTime) = date.toString
  }
}

/**
  * Enum of date use to filter query
  */
sealed trait QueryDateTime {
  val date: LocalDateTime
  val startRange: LocalDateTime
  val endRange: LocalDateTime

  def filter(localDateTime: LocalDateTime): Boolean = (localDateTime isEqual startRange) || (localDateTime isAfter startRange) && (localDateTime isBefore endRange)
}

case class Year(date: LocalDateTime) extends QueryDateTime {
  override val startRange = date.withDayOfYear(1)
  override val endRange = date.plusYears(1).withDayOfYear(1)
}

case class Month(date: LocalDateTime) extends QueryDateTime {
  override val startRange = date.withDayOfMonth(1)
  override val endRange = date.plusMonths(1).withDayOfMonth(1)
}

case class Day(date: LocalDateTime) extends QueryDateTime {
  override val startRange = date.withHour(0)
  override val endRange = date.plusDays(1).withHour(0)
}

case class Hour(date: LocalDateTime) extends QueryDateTime {
  override val startRange = date.withMinute(0)
  override val endRange = date.plusHours(1)
}

case class Minute(date: LocalDateTime) extends QueryDateTime {
  override val startRange = date.withSecond(0)
  override val endRange = date.plusMinutes(1).withSecond(0)
}