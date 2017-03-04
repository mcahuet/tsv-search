package utils

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}

trait BaseSpec extends PlaySpec with OneAppPerSuite with MockitoSugar {
}
