package components

import java.time.LocalDateTime

import utils.BaseSpec

class QueryTemporalSpec extends BaseSpec {


  "QueryTemporal" must {
    "not parse bad date format" in {

      val temporalBadYear = QueryTemporal.unapply("23")
      val temporalBadMonth = QueryTemporal.unapply("2015 03")
      val temporalBadDay = QueryTemporal.unapply("2015-03 17")
      val temporalBadHour = QueryTemporal.unapply("2015-03-17T04")
      val temporalBadMinute = QueryTemporal.unapply("2015-03-17 04 06")

      temporalBadYear must be (None)
      temporalBadMonth must be (None)
      temporalBadDay must be (None)
      temporalBadMinute must be (None)
    }

    "parse year" in {
      val inRangeDate = LocalDateTime.of(2015,4,6,0,0,0)
      val outRangeDate = LocalDateTime.of(2016,1,1,0,0,0)
      val temporal = QueryTemporal.unapply("2015")

      temporal must be (Some(Year(LocalDateTime.of(2015,1,1,0,0,0))))
      temporal.get.startRange must be (LocalDateTime.of(2015,1,1,0,0,0))
      temporal.get.endRange must be (LocalDateTime.of(2016,1,1,0,0,0))
      temporal.get.filter(inRangeDate) must be (true)
      temporal.get.filter(outRangeDate) must be (false)
    }

    "parse month" in {
      val inRangeDate = LocalDateTime.of(2015,5,6,0,0,0)
      val outRangeDate = LocalDateTime.of(2015,6,1,0,0,0)
      val temporal = QueryTemporal.unapply("2015-05")

      temporal must be (Some(Month(LocalDateTime.of(2015,5,1,0,0,0))))
      temporal.get.startRange must be (LocalDateTime.of(2015,5,1,0,0,0))
      temporal.get.endRange must be (LocalDateTime.of(2015,6,1,0,0,0))
      temporal.get.filter(inRangeDate) must be (true)
      temporal.get.filter(outRangeDate) must be (false)
    }

    "parse day" in {
      val inRangeDate = LocalDateTime.of(2015,5,4,0,0,0)
      val outRangeDate = LocalDateTime.of(2015,5,5,0,0,0)
      val temporal = QueryTemporal.unapply("2015-05-04")

      temporal must be (Some(Day(LocalDateTime.of(2015,5,4,0,0,0))))
      temporal.get.startRange must be (LocalDateTime.of(2015,5,4,0,0,0))
      temporal.get.endRange must be (LocalDateTime.of(2015,5,5,0,0,0))
      temporal.get.filter(inRangeDate) must be (true)
      temporal.get.filter(outRangeDate) must be (false)
    }

    "parse hour" in {
      val inRangeDate = LocalDateTime.of(2015,5,4,14,0,0)
      val outRangeDate = LocalDateTime.of(2015,5,4,15,0,0)
      val temporal = QueryTemporal.unapply("2015-05-04 14")

      temporal must be (Some(Hour(LocalDateTime.of(2015,5,4,14,0,0))))
      temporal.get.startRange must be (LocalDateTime.of(2015,5,4,14,0,0))
      temporal.get.endRange must be (LocalDateTime.of(2015,5,4,15,0,0))
      temporal.get.filter(inRangeDate) must be (true)
      temporal.get.filter(outRangeDate) must be (false)
    }

    "parse minute" in {
      val inRangeDate = LocalDateTime.of(2015,5,4,14,24,0)
      val outRangeDate = LocalDateTime.of(2015,5,4,14,25,0)
      val temporal = QueryTemporal.unapply("2015-05-04 14:24")

      temporal must be (Some(Minute(LocalDateTime.of(2015,5,4,14,24,0))))
      temporal.get.startRange must be (LocalDateTime.of(2015,5,4,14,24,0))
      temporal.get.endRange must be (LocalDateTime.of(2015,5,4,14,25,0))
      temporal.get.filter(inRangeDate) must be (true)
      temporal.get.filter(outRangeDate) must be (false)
    }
  }
}
