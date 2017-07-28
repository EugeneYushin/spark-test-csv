package my.fancy.organization.step.impl

import com.typesafe.scalalogging.LazyLogging
import my.fancy.organization.spark.functions.FilterEmptyOrBlankStrings
import my.fancy.organization.step.Step
import org.apache.spark.sql.DataFrame

class FilterStep extends Step with LazyLogging {
  override def run(df: DataFrame): DataFrame = {
    logger.info("----------------")
    logger.info("Run FILTER")
    df.filter(new FilterEmptyOrBlankStrings)
  }
}
