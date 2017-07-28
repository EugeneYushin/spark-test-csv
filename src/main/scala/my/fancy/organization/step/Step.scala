package my.fancy.organization.step

import org.apache.spark.sql.DataFrame

/**
  * Processing step
  */
trait Step {
  /**
    * Submit step
    * @param df Input Spark Dataframe
    * @return Processed Dataframe
    */
  def run(df: DataFrame): DataFrame
}
