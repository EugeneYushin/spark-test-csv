package my.fancy.organization

import com.typesafe.config.Config
import org.json4s.JValue
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import scala.collection.JavaConversions._

/**
  * Utility object. It's possible to use package object for the same purpose,
  * although it will result in organizing sub-packages into nested structure (since Scala 2.8)
  */
object Utils {
  implicit val formats = org.json4s.DefaultFormats

  /**
    * Concert config to Map
    *
    * @param config Config instance
    * @return KeyValue mapping
    */
  def confToMap(config: Config): Map[String, String] = {
    // Unlike 'toString', 'asInstanceOf' won't allow to pass non-string values in config file.
    // Although, 'asInstanceOf' is more safe. So it's preferable to allow only string values
    // for Spark reader config options
    config.entrySet().map(entry => (entry.getKey, config.getAnyRef(entry.getKey).toString)).toMap
  }

  /**
    * Convert Key-Value pair to JSON instance
    *
    * @param key   Key
    * @param value Value
    * @return
    */
  def kvToJson(key: Any, value: Long): JValue = {
    render(key.toString -> value)
  }
}
