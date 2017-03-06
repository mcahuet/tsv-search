package components


class ReaderSpec extends BaseSpec{

  private val pathFile = app.configuration.underlying.getString("query.file.path")
  private val tsvSeparator = "\t"

  "CsvReader" must {
    "return stream" in {

      val stream = Reader.read(pathFile, tsvSeparator)

      stream.isRight must be(true)
    }
    "return error message" in {
      val stream = Reader.read("test", tsvSeparator)

      stream.isLeft must be(true)
    }

    "return not empty stream and contains file lines" in {

      val stream = Reader.read(pathFile, tsvSeparator)

      stream.right.map(_.size) must not be 0
    }
  }
}
