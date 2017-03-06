package components


class ReaderSpec extends BaseSpec {

  private val pathFile = app.configuration.underlying.getString("query.file.path")
  private val tsvSeparator = "\t"

  "Reader" must {
    "return stream" in {

      val stream = Reader.read(pathFile, tsvSeparator)

      stream.isDefined must be (true)
    }
    "return error message" in {
      val stream = Reader.read("test", tsvSeparator)

      stream must be (None)
    }

    "return not empty stream and contains file lines" in {

      val stream = Reader.read(pathFile, tsvSeparator)

      stream.map(_.size) must not be 0
    }
  }
}
