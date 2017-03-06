package components.filters

import javax.inject.Inject

import play.api.http.HttpFilters

/**
  * All filters of application
  * @param log is the filter which log all request
  */
class Filters @Inject()(log: RequestLoggingFilter) extends HttpFilters {
  def filters = Seq(log)
}