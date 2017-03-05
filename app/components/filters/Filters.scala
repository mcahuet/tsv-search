package components.filters

import javax.inject.Inject

import play.api.http.HttpFilters


class Filters @Inject()(log: LoggingFilter) extends HttpFilters {
  def filters = Seq(log)
}