package my.fancy.organization.spark.functions

import org.apache.spark.api.java.function.FilterFunction
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StringType

/**
  * Filter blank strings and strings containing blank spaces only
  */
class FilterEmptyOrBlankStrings extends FilterFunction[Row] {

  override def call(value: Row): Boolean = {
    val schema = value.schema

    schema.fields.map(f => {
      f.dataType match {
        case StringType =>
          val fieldValue = value.getAs[String](f.name)
          fieldValue == null || !fieldValue.trim.isEmpty
        case _ => true
      }
    }).reduce(_ && _)
  }
}
