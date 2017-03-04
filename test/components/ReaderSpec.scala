package components

import utils.BaseSpec

class ReaderSpec extends BaseSpec {

  private val reader = new CsvReader
  private val pathFile = "conf/resources/hn_logs_test.tsv"
  private val tsvSeparator = "\t"

  "CsvReader" must {
    "return stream" in {

      val stream = reader.read(pathFile, tsvSeparator)

      stream.isRight must be(true)
    }
    "return error message" in {
      val stream = reader.read("test", tsvSeparator)

      stream.isLeft must be(true)
    }

    "return not empty stream and contains file lines" in {

      val stream = reader.read(pathFile, tsvSeparator)

      stream.right.map(_.size) must not be 0
    }
  }
}
