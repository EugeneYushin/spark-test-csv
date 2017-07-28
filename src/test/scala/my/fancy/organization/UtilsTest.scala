package my.fancy.organization

import com.typesafe.config.ConfigValueFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

class UtilsTest extends FlatSpec with Matchers {
  "confToMap" should "return Map for passed Config" in {
    val expected = Map("option" -> "value")
    val conf = ConfigValueFactory.fromMap(expected.asJava).toConfig

    val result = Utils.confToMap(conf)

    result should equal(expected)
  }

  "confToMap" should "return String representation on non-String value in Config" in {
    val expected = Map("option" -> "true")
    val conf = ConfigValueFactory.fromMap(Map("option" -> true).asJava).toConfig

    val result = Utils.confToMap(conf)

    result should equal(expected)
  }
}
