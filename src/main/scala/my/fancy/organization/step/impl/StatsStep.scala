package my.fancy.organization
package step.impl

import com.typesafe.scalalogging.LazyLogging
import my.fancy.organization.beans.Stats
import my.fancy.organization.step.Step
import org.apache.spark.sql.DataFrame
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

import scala.collection.JavaConverters._

class StatsStep() extends Step with LazyLogging {
  override def run(df: DataFrame): DataFrame = {
    logger.info("----------------")
    logger.info("Run STATS")
    import df.sparkSession.implicits._

    df.schema.fieldNames.map(field => {
      val nonNull = df.select(field).filter($"$field".isNotNull)
      val uniqueValues = nonNull
        .groupBy(field)
        .count()
        .collectAsList().asScala
        .map(row => {
          Utils.kvToJson(row.getAs[Any](0), row.getLong(1))
        })

//      val cDistinct = df.select(countDistinct(field) as "cDistinct").collect()(0).getLong(0)
      val cDistinct = nonNull.distinct().count()
      val output = ("Column" -> field) ~ ("Unique_values" -> cDistinct) ~ ("Values" -> uniqueValues)

      Stats(compact(render(output)))
    }).toSeq.toDF()
  }
}
